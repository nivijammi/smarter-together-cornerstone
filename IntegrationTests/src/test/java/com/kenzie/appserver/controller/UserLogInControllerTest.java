package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.AddUserLoginResponse;
import com.kenzie.appserver.controller.model.RegistrationStatus;
import com.kenzie.appserver.controller.model.UserLoginRequest;
import com.kenzie.appserver.controller.model.UserRegistrationResponse;
import com.kenzie.appserver.service.MemberService;
import com.kenzie.appserver.service.UserLoginService;
import com.kenzie.appserver.service.model.Member;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class UserLogInControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private MemberService memberService;


    private final MockNeat mockNeat = MockNeat.threadLocal();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * ------------------------------------------------------------------------
     * register user
     * ------------------------------------------------------------------------
     * <p>
     * Acceptance criteria: user is registered
     * Endpoint(s) tested: "/v1/users/register"
     * GIVEN (Preconditions): valid email and password
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 201 code, user is logged in and authenticated
     * Clean up : restore the state of system back to original state
     */

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
        userLoginService.isValidEmail(email);
        userLoginService.isPasswordStrengthGood(password);
        userLoginService.hashPassword(password);
        userLoginService.doesUserExist(email);


        ResultActions actions = mvc.perform(post("/v1/users/login")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());


        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);
        AddUserLoginResponse response = mapper.readValue(responseBody, AddUserLoginResponse.class);
        //System.out.println("nj "+response.getRegistrationStatus());

        // Assertions
        assertEquals(email, response.getUserEmail());
        assertEquals(RegistrationStatus.LOGIN_SUCCESSFUL, response.getRegistrationStatus());


    }

    /**
     * Acceptance criteria: login unsuccessful
     * Endpoint(s) tested: "/v1//users/login"
     * GIVEN (Preconditions): valid email and invalid password
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 400 code
     * Clean up : restore the state of system back to original state
     */
    @Test
    public void userLogin_passwordInvalid_failure() throws Exception {
        String email = "person1@aol.com";
        String password = "InvalidPassword";

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        // user not saved with hashed password
        Member user = new Member(email, password);

        userLoginService.registerUser(user);
        userLoginService.isValidEmail(email);
        userLoginService.isPasswordStrengthGood(password);
        userLoginService.hashPassword(password);
        userLoginService.doesUserExist(email);

        // WHEN
        ResultActions actions = mvc.perform(post("/v1/users/login")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Acceptance criteria: login unsuccessful
     * Endpoint(s) tested: "/v1//users/login"
     * GIVEN (Preconditions): invalid email and valid password
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 400 code
     * Clean up : restore the state of system back to original state
     */

    @Test
    public void userLogin_emailInvalid_failure() throws Exception {
        String email = "invalidEmail";
        String password = "Password3!";
        String hashedPassword = "08676262827036899e54c34f312486111e1952559899acdd0f604b8e4b221e03";

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        Member user = new Member(email, hashedPassword);

        userLoginService.registerUser(user);
        userLoginService.isValidEmail(email);
        userLoginService.isPasswordStrengthGood(password);
        userLoginService.hashPassword(password);
        userLoginService.doesUserExist(email);

        // WHEN
        ResultActions actions = mvc.perform(post("/v1/users/login")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    /**
     * Acceptance criteria: login unsuccessful
     * Endpoint(s) tested: "/v1//users/login"
     * GIVEN (Preconditions): valid email, valid password but user does not exist
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 400 code
     * Clean up : restore the state of system back to original state
     */
    @Test
    public void userLogin_userDoesNotExist_failure() throws Exception {
        String email = "invalidEmail";
        String password = "Password3!";
        String hashedPassword = "08676262827036899e54c34f312486111e1952559899acdd0f604b8e4b221e03";

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        Member user = new Member(email, hashedPassword);

        // user not registered - so cannot login
        userLoginService.isValidEmail(email);
        userLoginService.isPasswordStrengthGood(password);
        userLoginService.hashPassword(password);
        userLoginService.doesUserExist(email);


        // WHEN
        ResultActions actions = mvc.perform(post("/v1/users/login")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());


    }

    /**
     * ------------------------------------------------------------------------
     * User logs in - Authentication
     * ------------------------------------------------------------------------
     * <p>
     * Acceptance criteria: user registered successfully
     * Endpoint(s) tested: "/v1/users/register"
     * GIVEN (Preconditions): valid email, password
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 2xx code, user registered
     */
    @Test
    public void userRegistration_successful() throws Exception {
        String email = "newPerson@aol.com";
        String password = "Password3!";

        userLoginService.doesUserExist(email);
        userLoginService.isValidEmail(email);
        userLoginService.isPasswordStrengthGood(password);
        //userLoginService.hashPassword(password);

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResultActions actions = mvc.perform(post("/v1/users/register")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);
        UserRegistrationResponse response = mapper.readValue(responseBody, UserRegistrationResponse.class);
        //System.out.println("nj "+response.getRegistrationStatus());

        // Assertions
        assertEquals(email, response.getUserEmail());
        assertEquals(RegistrationStatus.USER_REGISTERED, response.getRegistrationStatus());

    }

    /**
     * Acceptance criteria: user registration unsuccessful
     * Endpoint(s) tested: "/v1/users/register"
     * GIVEN (Preconditions): invalid password
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 4xx code
     */
    @Test
    public void userRegistration_invalidPassword_unsuccessful() throws Exception {
        String email = "person2@aol.com";
        String password = "password";

        userLoginService.isValidEmail(email);
        userLoginService.isPasswordStrengthGood(password);
        userLoginService.hashPassword(password);

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResultActions actions = mvc.perform(post("/v1/users/register")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Assertions
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        UserRegistrationResponse errorResponse = mapper.readValue(responseBody, UserRegistrationResponse.class);

        assertEquals("Invalid password", errorResponse.getMessage());
        assertEquals(RegistrationStatus.REGISTRATION_UNSUCCESSFUL, errorResponse.getRegistrationStatus());

    }

    /**
     * Acceptance criteria: user registration unsuccessful
     * Endpoint(s) tested: "/v1/users/register"
     * GIVEN (Preconditions): invalid email
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 4xx code
     */

    @Test
    public void userRegistration_invalidEmail_unsuccessful() throws Exception {
        String email = "invalid_email";
        String password = "Password3!";

        // userLoginService.doesUserExist(email);
        userLoginService.isValidEmail(email);
        userLoginService.isPasswordStrengthGood(password);
        userLoginService.hashPassword(password);

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResultActions actions = mvc.perform(post("/v1/users/register")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Assertions
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        UserRegistrationResponse errorResponse = mapper.readValue(responseBody, UserRegistrationResponse.class);

        assertEquals("Invalid email", errorResponse.getMessage());
        assertEquals(RegistrationStatus.REGISTRATION_UNSUCCESSFUL, errorResponse.getRegistrationStatus());
    }


    /**
     * Acceptance criteria: user registration unsuccessful
     * Endpoint(s) tested: "/v1/users/register"
     * GIVEN (Preconditions): user already exists
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 4xx code
     */
    @Test
    public void userRegistration_userAlreadyExists_unsuccessful() throws Exception {
        String email = "person1@aol.com";
        String password = "Password3!";
        String hashedPassword = "08676262827036899e54c34f312486111e1952559899acdd0f604b8e4b221e03";

        Member member = new Member(email,hashedPassword);

       userLoginService.registerUser(member);

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        ResultActions actions = mvc.perform(post("/v1/users/register")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        // Assertions
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        UserRegistrationResponse errorResponse = mapper.readValue(responseBody, UserRegistrationResponse.class);

        assertEquals("Email already exists", errorResponse.getMessage());
        assertEquals(RegistrationStatus.REGISTRATION_UNSUCCESSFUL, errorResponse.getRegistrationStatus());
    }
}
