package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.AddUserLoginResponse;
import com.kenzie.appserver.controller.model.RegistrationStatus;
import com.kenzie.appserver.controller.model.UserLoginRequest;
import com.kenzie.appserver.controller.model.UserRegistrationResponse;
import com.kenzie.appserver.service.UserAyncLoginService;
import com.kenzie.appserver.service.UserLoginService;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.MemberValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow
 */
@RestController
@RequestMapping("/v2")
public class UserAsyncLogInController {

    @Autowired
    UserAyncLoginService userAyncLoginService;
    @Autowired
    UserLoginService userLoginService;

    @GetMapping("/users/{groupId}")
    public CompletableFuture<List<String>> getAllRegisteredUsers(@PathVariable String groupId) {
        CompletableFuture<List<String>> response = userAyncLoginService.getSomethingAsync(groupId);
        return  response;
    }

    @PostMapping("/users/login")
    @ResponseBody
    public CompletableFuture<AddUserLoginResponse> loginAsync(@RequestBody UserLoginRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        // Email invalid
        if (!userLoginService.isValidEmail(email)) {
            String errorMessage = "Invalid email format";
            AddUserLoginResponse emailInvalidResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_SUCCESSFUL,errorMessage);
            //return ResponseEntity.status(400).body(emailInvalidResponse);
            return CompletableFuture.completedFuture(emailInvalidResponse);
        }

        // Password don't match
        if (!userLoginService.isPasswordStrengthGood(password)) {
            String errorMessage = "Password strength is weak";
            AddUserLoginResponse passwordStrengthInvalidResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_UNSUCCESSFUL,errorMessage);
            //return ResponseEntity.status(400).body(passwordStrengthInvalidResponse);
            return CompletableFuture.completedFuture(passwordStrengthInvalidResponse);
        }

        // Authenticate the user
        //CompletableFuture<MemberValidationStatus> status = userAyncLoginService.authenticateUserAsync(email, password);
        CompletableFuture<AddUserLoginResponse> addUserLoginResponseCompletableFuture = userAyncLoginService.authenticateUserAsync(email, password)
                // checking the MemberValidationStatus to return the AddUserLoginResponse
                .thenCompose(status -> returnResponse(request.getEmail(), status));
        return addUserLoginResponseCompletableFuture;

        //    // Scenario #1: User not found
        //    if (!status.isUserFound() && !status.isPasswordMatched()) {
        //        String errorMessage = "User not found and password does not match";
        //        AddUserLoginResponse userNotFoundResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_UNSUCCESSFUL,errorMessage);
        //        return ResponseEntity.status(400).body(userNotFoundResponse);
        //    }
        //
        //    // Password does not match -
        //    else if (status.isUserFound() && !status.isPasswordMatched()){
        //        String errorMessage = "Invalid Password";
        //        AddUserLoginResponse passwordNotMatchedResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_UNSUCCESSFUL,errorMessage );
        //        return ResponseEntity.status(400).body(passwordNotMatchedResponse);
        //    }
        //
        //    // User found and password matches
        //    else{
        //        String message = "Login successful";
        //        AddUserLoginResponse loginSuccessfulResponse  = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_SUCCESSFUL, message);
        //        return ResponseEntity.created(URI.create("/users/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
        //    }
    }

    private CompletionStage<AddUserLoginResponse> returnResponse(String email, MemberValidationStatus status) {

        // Scenario #1: User not found
        if (!status.isUserFound() && !status.isPasswordMatched()) {
            String errorMessage = "User not found and password does not match";
            //AddUserLoginResponse userNotFoundResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_UNSUCCESSFUL,errorMessage);
            AddUserLoginResponse userNotFoundResponse = new AddUserLoginResponse(email, RegistrationStatus.LOGIN_UNSUCCESSFUL,errorMessage);
            return CompletableFuture.completedFuture(userNotFoundResponse);
        }

        // Password does not match -
        else if (status.isUserFound() && !status.isPasswordMatched()){
            String errorMessage = "Invalid Password";
            AddUserLoginResponse passwordNotMatchedResponse = new AddUserLoginResponse(email, RegistrationStatus.LOGIN_UNSUCCESSFUL,errorMessage );
            return CompletableFuture.completedFuture(passwordNotMatchedResponse);
            //return ResponseEntity.status(400).body(passwordNotMatchedResponse);
        }

        // User found and password matches
        else{
            String message = "Login successful";
            AddUserLoginResponse loginSuccessfulResponse  = new AddUserLoginResponse(email, RegistrationStatus.LOGIN_SUCCESSFUL, message);
            //return ResponseEntity.created(URI.create("/users/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
            return CompletableFuture.completedFuture(loginSuccessfulResponse);
        }
    }

    @PostMapping("/users/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserLoginRequest request) {

        // Scenario #1: User Exists
        boolean exists = userLoginService.doesUserExist(request.getEmail());
        if (exists){
            System.out.println("Email already exists");
            UserRegistrationResponse userExistsResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.REGISTRATION_UNSUCCESSFUL,"Email already exists");
            return ResponseEntity.status(400).body(userExistsResponse);
        }

        // Scenario #2: Email not valid
        if (!userLoginService.isValidEmail(request.getEmail())) {
            System.out.println("Email is not valid");
            UserRegistrationResponse invalidEmailResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.REGISTRATION_UNSUCCESSFUL,"Invalid email");
            return ResponseEntity.status(400).body(invalidEmailResponse);
        }

        // Scenario #3: Password not valid
        if (!userLoginService.isPasswordStrengthGood(request.getPassword())) {
            System.out.println("Password strength is weak");
            UserRegistrationResponse invalidPasswordResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.REGISTRATION_UNSUCCESSFUL,"Invalid password");
            return ResponseEntity.status(400).body(invalidPasswordResponse);
        }

        // Scenario #4: Hash the userPassword, and store it in the db
        String hashedPassword = userLoginService.hashPassword(request.getPassword());
        Member user = new Member(request.getEmail(),hashedPassword);
        Member newUser = userLoginService.registerUser(user);

        UserRegistrationResponse loginSuccessfulResponse = new UserRegistrationResponse(newUser.getEmail(), RegistrationStatus.USER_REGISTERED,"Registration successful");
        return ResponseEntity.created(URI.create("/users/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
    }


}
