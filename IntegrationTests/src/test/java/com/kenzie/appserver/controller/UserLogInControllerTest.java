package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.service.UserLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
public class UserLogInControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserLoginService userLoginService;

    @Test
    public void userLogin_success(){

    }


}
