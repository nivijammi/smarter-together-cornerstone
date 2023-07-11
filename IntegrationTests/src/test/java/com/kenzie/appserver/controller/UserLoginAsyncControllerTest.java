package com.kenzie.appserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.UserAsyncLogInController;
import com.kenzie.appserver.controller.model.AddUserLoginResponse;
import com.kenzie.appserver.controller.model.RegistrationStatus;
import com.kenzie.appserver.controller.model.UserLoginRequest;
import com.kenzie.appserver.service.UserLoginService;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.MemberValidationStatus;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
public class UserLoginAsyncControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserAsyncLogInController userAsyncLogInController;

    @Autowired
    private UserLoginService userLoginService;


    private final MockNeat mockNeat = MockNeat.threadLocal();
    private final ObjectMapper mapper = new ObjectMapper();

    // https://www.baeldung.com/java-completablefuture
    // youtube videos on multithreading by CodeCepts


    @Test
    public void userLogin_success() throws Exception {
        String email = "person1@aol.com";
        String hashedPassword = "08676262827036899e54c34f312486111e1952559899acdd0f604b8e4b221e03";
        String password = "Password3!";

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        Member user = new Member(email, hashedPassword);

        userLoginService.registerUser(user);

        CompletableFuture<AddUserLoginResponse> responseFuture = userAsyncLogInController.loginAsync(request);

        CompletableFuture<MemberValidationStatus> memberValidationStatusFuture = responseFuture
                .thenApply(response -> {
                    assertTrue(response.getRegistrationStatus() == RegistrationStatus.LOGIN_SUCCESSFUL);
                    assertEquals(email, response.getUserEmail());
                    return new MemberValidationStatus(true, true);
                });

        AddUserLoginResponse response = responseFuture.get();

        // Assertions
        assertEquals(email, response.getUserEmail());
        assertEquals(RegistrationStatus.LOGIN_SUCCESSFUL, response.getRegistrationStatus());



    }





}
