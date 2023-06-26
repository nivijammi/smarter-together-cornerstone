package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.UserLoginService;
//import com.kenzie.appserver.service.UserProfileService;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.MemberValidationStatus;
import com.kenzie.appserver.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.ZonedDateTime;

/**
 * https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow
 */
@RestController
@RequestMapping("/users")
public class UserLogInController {
    @Autowired
    UserLoginService userLoginService;

//    @Autowired
//    UserProfileService userProfileService;

    // loged in or unsuccessful
    @PostMapping("/login")
    public ResponseEntity<AddUserLoginResponse> login(@RequestBody UserLoginRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        // Check if the email and password are valid
        if (!userLoginService.isValidEmail(email)) {
            AddUserLoginResponse loginSuccessfulResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.EMAIL_INVALID);
            return ResponseEntity.created(URI.create("/users/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
        }

        if (!userLoginService.isPasswordStrengthGood(password)) {
            AddUserLoginResponse loginSuccessfulResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.PASSWORD_NOT_MATCHED);
            return ResponseEntity.created(URI.create("/users/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
        }

        // Authenticate the user
        MemberValidationStatus status = userLoginService.authenticateUser(email, password);

        // Scenario #1: User not found
        if (!status.isUserFound() && !status.isPasswordMatched()) {
            AddUserLoginResponse userNotFoundResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.USER_NOT_FOUND);
            return ResponseEntity.created(URI.create("/users/" + userNotFoundResponse.getUserEmail())).body(userNotFoundResponse);
        }

        // Password does not match -
        else if (status.isUserFound() && !status.isPasswordMatched()){
            AddUserLoginResponse passwordNotMatchedResponse = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.PASSWORD_NOT_MATCHED);
            return ResponseEntity.created(URI.create("/users/" + passwordNotMatchedResponse.getUserEmail())).body(passwordNotMatchedResponse);
        }

        // User found and password matches
        else{
            AddUserLoginResponse loginSuccessfulResponse  = new AddUserLoginResponse(request.getEmail(), RegistrationStatus.LOGIN_SUCCESSFUL);
            return ResponseEntity.created(URI.create("/users/" + loginSuccessfulResponse.getUserEmail())).body(loginSuccessfulResponse);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserLoginRequest request) {

        // Scenario #1: User Exists
        boolean exists = userLoginService.doesUserExist(request.getEmail());
        if (exists){
            UserRegistrationResponse userExistsResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.EMAIL_ALREADY_EXISTS);
            return ResponseEntity.created(URI.create("/users/" + userExistsResponse.getUserEmail())).body(userExistsResponse);
        }

        // Scenario #2: Email not valid
        if (!userLoginService.isValidEmail(request.getEmail())) {
            UserRegistrationResponse invalidEmailResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.EMAIL_INVALID);
            return ResponseEntity.created(URI.create("/users/" + invalidEmailResponse.getUserEmail())).body(invalidEmailResponse);
        }

        // Scenario #3: Password not valid
        if (!userLoginService.isPasswordStrengthGood(request.getPassword())) {
            UserRegistrationResponse invalidPasswordResponse = new UserRegistrationResponse(request.getEmail(), RegistrationStatus.PASSWORD_NOT_MATCHED);
            return ResponseEntity.created(URI.create("/users/" + invalidPasswordResponse.getUserEmail())).body(invalidPasswordResponse);
        }

        // Scenario #4: Hash the userPassword, and store it in the db
        String hashedPassword = userLoginService.hashPassword(request.getPassword());
        Member user = new Member(request.getEmail(),hashedPassword);
        Member newUser = userLoginService.registerUser(user);
//
//        User userProfile = new User(request.getEmail(),hashedPassword,ZonedDateTime.now());
//        User newUserProfile = userProfileService.createUserProfile(userProfile);

        UserRegistrationResponse loginSuccessfullResponse = new UserRegistrationResponse(newUser.getEmail(), RegistrationStatus.USER_REGISTERED);
        return ResponseEntity.created(URI.create("/users/" + loginSuccessfullResponse.getUserEmail())).body(loginSuccessfullResponse);
    }

//    @PutMapping("/{userId}")
//    public ResponseEntity<UserProfileResponse> updateUserProfile(@PathVariable String email, @RequestBody UserProfileRequest userProfileRequest) {
//        if (userProfileRequest.getEmail() == null || userProfileRequest.getEmail().isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Email is required");
//        }
//
//        User existingUserProfile = userProfileService.findUserProfileByEmail(email);
//        if (existingUserProfile == null) {
//            return ResponseEntity.notFound().build();
//        }
//        existingUserProfile.setEmail(userProfileRequest.getEmail());
//        existingUserProfile.setPassword(userProfileRequest.getPassword());
//        existingUserProfile.setCreationDate(userProfileRequest.getCreationDate());
//
//        userProfileService.updateUserProfile(existingUserProfile);
//
//        UserProfileResponse userProfileResponse = convertToUserProfileResponse(existingUserProfile);
//        return ResponseEntity.ok(userProfileResponse);
//
//
//    }
//
//    @DeleteMapping("/{userID}")
//    public ResponseEntity deleteUserProfile(@PathVariable String email) {
//
//        User existingUserProfile = userProfileService.findUserProfileByEmail(email);
//        if (existingUserProfile == null) {
//            return ResponseEntity.notFound().build();
//        }
//        userProfileService.deleteUserProfile(email);
//        return ResponseEntity.noContent().build();
//    }
//
//    private UserProfileResponse convertToUserProfileResponse(User user) {
//        UserProfileResponse response = new UserProfileResponse();
//        response.setEmail(user.getEmail());
//        response.setPassword(user.getPassword());
//        response.setCreationDate(user.getCreationDate());
//
//        return response;
//    }
}
