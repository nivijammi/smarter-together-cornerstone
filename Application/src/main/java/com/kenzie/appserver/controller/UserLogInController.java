package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.AddUserLoginResponse;
import com.kenzie.appserver.controller.model.RegistrationStatus;
import com.kenzie.appserver.controller.model.UserLoginRequest;
import com.kenzie.appserver.controller.model.UserRegistrationResponse;
import com.kenzie.appserver.service.UserLoginService;
import com.kenzie.appserver.service.model.UserDetail;
import com.kenzie.appserver.service.model.UserValidationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow
 */
@RestController
@RequestMapping("/user")
public class UserLogInController {
    @Autowired
    UserLoginService userLoginService;


    @PostMapping("/login")
    public ResponseEntity<AddUserLoginResponse> login(@RequestBody UserLoginRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        // Check if the email and password are valid
        if (!userLoginService.isValidEmail(email)) {
            AddUserLoginResponse loginSuccessfulResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.EMAIL_INVALID);
            return ResponseEntity.created(URI.create("/user/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
        }

        if (!userLoginService.isPasswordStrengthGood(password)) {
            AddUserLoginResponse loginSuccessfulResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.PASSWORD_NOT_MATCHED);
            return ResponseEntity.created(URI.create("/user/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
        }

        // Authenticate the user
        UserValidationStatus status = userLoginService.authenticateUser(email, password);

        // Scenario #1: User not found
        if (!status.isUserFound() && !status.isPasswordMatched()) {
            AddUserLoginResponse userNotFoundResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.USER_NOT_FOUND);
            return ResponseEntity.created(URI.create("/user/" + userNotFoundResponse.getUserEmail())).body(userNotFoundResponse);
        }

        // Password does not match
        else if (status.isUserFound() && !status.isPasswordMatched()){
            AddUserLoginResponse passwordNotMatchedResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.PASSWORD_NOT_MATCHED);
            return ResponseEntity.created(URI.create("/user/" + passwordNotMatchedResponse.getUserEmail())).body(passwordNotMatchedResponse);
        }

        // User found and password matches
        else{
            AddUserLoginResponse loginSuccessfulResponse  = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_SUCCESSFUL);
            return ResponseEntity.created(URI.create("/user/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserLoginRequest request) {

        // Scenario #1: User Exists
        boolean exists = userLoginService.doesUserExist(request.getEmail());
        if (exists){
            UserRegistrationResponse userExistsResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.EMAIL_EXISTS);
            return ResponseEntity.created(URI.create("/user/" + userExistsResponse.getUserEmail())).body(userExistsResponse);
        }

        // Scenario #2: Email not valid
        if (!userLoginService.isValidEmail(request.getEmail())) {
            UserRegistrationResponse invalidEmailResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.EMAIL_INVALID);
            return ResponseEntity.created(URI.create("/user/" + invalidEmailResponse.getUserEmail())).body(invalidEmailResponse);
        }

        // Scenario #3: Password not valid
        if (!userLoginService.isPasswordStrengthGood(request.getPassword())) {
            UserRegistrationResponse invalidPasswordResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.PASSWORD_NOT_MATCHED);
            return ResponseEntity.created(URI.create("/user/" + invalidPasswordResponse.getUserEmail())).body(invalidPasswordResponse);
        }

        // Scenario #4: Hash the userPassword, and store it in the db
        String hashedPassword = userLoginService.hashPassword(request.getPassword());
        UserDetail user = new UserDetail(request.getEmail(),hashedPassword);
        UserDetail newUser = userLoginService.registerUser(user);

        UserRegistrationResponse loginSuccessfullResponse = new UserRegistrationResponse(newUser.getUserEmail(), RegistrationStatus.LOGIN_SUCCESSFUL);
        return ResponseEntity.created(URI.create("/user/" + loginSuccessfullResponse.getUserEmail())).body(loginSuccessfullResponse);
    }
}
