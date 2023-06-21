package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.AddUserLoginResponse;
import com.kenzie.appserver.controller.model.UserLoginRequest;
import com.kenzie.appserver.service.UserLoginService;
import com.kenzie.appserver.service.model.LogInValidationResult;
import com.kenzie.appserver.service.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserLogInController {
    @Autowired
    UserLoginService userLoginService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        String email = request.getUsername();
        String password = request.getPassword();

        // Check if the email and password are valid
        if (!userLoginService.isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        if (!userLoginService.isValidPassword(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password format");
        }

        // Authenticate the user
        LogInValidationResult logInValidationResult = userLoginService.authenticateUser(email, password);
        if (logInValidationResult.equals("2000")) {
            return ResponseEntity.ok("Authentication successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping
    public ResponseEntity<AddUserLoginResponse> addNewUser(@RequestBody UserLoginRequest request) {
        UserDetail user = new UserDetail(request.getUsername(),request.getPassword());

        userLoginService.addNewUserDetails(user);

        AddUserLoginResponse response = new AddUserLoginResponse();
        response.setUserId(UUID.randomUUID().toString());
        response.setUserEmail(user.getUserEmail());
        response.setUserPassword(user.getUserPassWord());

        return ResponseEntity.created(URI.create("/user/" + response.getUserId())).body(response);

    }
}
