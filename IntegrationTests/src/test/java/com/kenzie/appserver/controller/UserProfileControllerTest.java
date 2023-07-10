package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.UserProfileRequest;
import com.kenzie.appserver.controller.model.UserProfileResponse;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import com.kenzie.appserver.service.UserService;
import com.kenzie.appserver.service.model.User;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class UserProfileControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserProfileController userProfileController;

    private final MockNeat mockNeat = MockNeat.threadLocal();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void addNewUser_addsANewUser() throws Exception {

        String email = mockNeat.emails().toString();
        String password = UUID.randomUUID().toString();
        String lastName = "Smith";
        String firstName = "John";
        ZonedDateTime date = ZonedDateTime.now();

        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setEmail(email);
        userProfileRequest.setPassword(password);
        userProfileRequest.setLastName(lastName);
        userProfileRequest.setFirstName(firstName);
        userProfileRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));

        ResultActions actions = mvc.perform(post("/v1/users")
                        .content(mapper.writeValueAsString(userProfileRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        UserProfileResponse response = mapper.readValue(responseBody, UserProfileResponse.class);
        assertThat(response.getEmail()).isNotEmpty().as("The email is populated");
        assertThat(response.getPassword()).isNotEmpty().as("The password is populated");
        assertThat(response.getLastName()).isEqualTo(userProfileRequest.getLastName()).as("The last name is correct");
        assertThat(response.getFirstName()).isEqualTo(userProfileRequest.getFirstName()).as("The first name is correct");
    }

    @Test
    public void addNewUser_addingNewUserFails() throws Exception {
        String email = mockNeat.emails().toString();
        String password = UUID.randomUUID().toString();
        String lastName = "Smith";
        String firstName = "John";
        ZonedDateTime date = ZonedDateTime.now();


        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setEmail(email);
        userProfileRequest.setPassword(password);
        userProfileRequest.setLastName(lastName);
        userProfileRequest.setFirstName(firstName);
        userProfileRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));

        mvc.perform(post("/v1/users")
                        //.content(mapper.writeValueAsString(userProfileRequest))// rest sets up the http request
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());// action
    }

    @Test
    public void updateUserProfile_success() throws Exception{
        String userId = mockNeat.emails().toString();//"someone@somebody.com"
        String email = mockNeat.emails().toString();;
        String password = "SomePassword9!";
        String lastName = "Smith";
        String firstName = "John";
        ZonedDateTime date = ZonedDateTime.now();

        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setEmail(email);
        userProfileRequest.setPassword(password);
        userProfileRequest.setLastName(lastName);
        userProfileRequest.setFirstName(firstName);
        userProfileRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));

        userService.addNewUser(userProfileRequest);

        ResultActions actions = mvc.perform(put("/v1/{email}",email)
                        .content(mapper.writeValueAsString(userProfileRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        UserProfileResponse response = mapper.readValue(responseBody, UserProfileResponse.class);
        assertThat(response.getEmail()).isEqualTo(email).as("The email is correct");
        assertThat(response.getPassword()).isEqualTo(password).as("The password is correct");
        assertThat(response.getLastName()).isEqualTo(userProfileRequest.getLastName()).as("The last name is correct");
        assertThat(response.getFirstName()).isEqualTo(userProfileRequest.getFirstName()).as("The first name is correct");

        userService.deleteUser(email);
    }

    @Test
    public void updateUserProfile_unsuccessful() throws Exception{

        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setPassword("password");
        userProfileRequest.setLastName("Smith");
        userProfileRequest.setPassword("John");
        userProfileRequest.setCreationDate(new ZonedDateTimeConverter().convert(ZonedDateTime.now()));

        String email = "smith@john.com";
        mvc.perform(put("/v1/{email}",email)
                        .content(mapper.writeValueAsString(userProfileRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void getUserById_successful() throws Exception {
        String email = "someone@somebody.com";
        String password = UUID.randomUUID().toString();
        String lastName = "Smith";
        String firstName = "John";
        ZonedDateTime date = ZonedDateTime.now();

        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setEmail(email);
        userProfileRequest.setPassword(password);
        userProfileRequest.setLastName(lastName);
        userProfileRequest.setFirstName(firstName);
        userProfileRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));

        // Add User
        userService.addNewUser(userProfileRequest);

        //THEN
        ResultActions actions = mvc.perform(get("/v1/{email}", email)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        UserProfileResponse response = mapper.readValue(responseBody, UserProfileResponse.class);
        assertThat(response.getEmail()).isNotEmpty().as("The user id is populated");
        assertThat(response.getPassword()).isNotEmpty().as("The password is populated");
        assertThat(response.getLastName()).isNotEmpty().as("The last name is populated");
        assertThat(response.getFirstName()).isNotEmpty().as("The first name is populated");

        userService.deleteUser(email);
    }

    @Test
    public void getUserById_unsuccessful() throws Exception {
        String email = "broken@email.com";

        //THEN
        mvc.perform(get("/v1/{email}", email)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteUserById_success() throws Exception {
        String email = "someone@somebody.com";
        String password = UUID.randomUUID().toString();
        String lastName = "Smith";
        String firstName = "John";
        ZonedDateTime date = ZonedDateTime.now();

        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setEmail(email);
        userProfileRequest.setPassword(password);
        userProfileRequest.setLastName(lastName);
        userProfileRequest.setFirstName(firstName);
        userProfileRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));

        // Add User
        userService.addNewUser(userProfileRequest);

        // WHEN
        mvc.perform(delete("/v1/{email}",email)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN //"/v1/notes/{noteId}"
        mvc.perform(get("/v1/{email}", email)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

//    @Test
//    public void deleteUserById_unsuccessful() throws Exception {
//
//        String email = "broken@email.com";
//
//        mvc.perform(delete("/v1/{userId}", email)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError());
//
//    }

    @Test
    public void deleteUserById_userNotFound() throws Exception {
        String email = "nonexistent@user.com";

        // WHEN
        mvc.perform(delete("/v1/{email}", email)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
