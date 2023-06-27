package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.AddStudyGroupRequest;
import com.kenzie.appserver.controller.model.AddStudyGroupResponse;
import com.kenzie.appserver.controller.model.StudyGroupMemberResponse;
import com.kenzie.appserver.controller.model.UserLoginRequest;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import com.kenzie.appserver.service.StudyGroupService;
import com.kenzie.appserver.service.model.StudyGroup;
import net.andreinc.mockneat.MockNeat;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * source: Q&A (June 26th,2023)
 * MVC is mocking the outside world - it simulate the behavior of an HTTP client and interact with
 * our RESTful API endpoints as if they were being called by an actual client.
 * It allows us to send requests, set request headers and content, and receive responses.
 * we can then perform assertions on the response to validate the behavior of your API.
 *
 * .content(mapper.writeValueAsString(studyGroupRequest)):
 * the request body content is set by converting the studyGroupRequest object to a JSON string using the ObjectMapper (mapper) instance.
 * This JSON representation of the studyGroupRequest object will be sent as the content of the request.
 *
 * .accept(MediaType.APPLICATION_JSON):
 * sets the Accept header of the request to specify that the client expects a response in JSON format.
 *
 * .contentType(MediaType.APPLICATION_JSON):
 * sets the Content-Type header of the request to specify that the content being sent in the request is in JSON format.
 *
 * .andExpect(status()....):
 * assertion that specifies the expected HTTP response status.
 * The status() method is used to access the response status
 */
@IntegrationTest
public class StudyGroupControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private StudyGroupService studyGroupService;
    @Autowired
    private UserLogInController userLogInController;
    private final MockNeat mockNeat = MockNeat.threadLocal();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void addStudyGroup_addsAStudyGroup() throws Exception {
        String groupId = UUID.randomUUID().toString();
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        StudyGroup studyGroup = new StudyGroup(groupId,groupName,discussionTopic,date,active);

        studyGroupService.addNewStudyGroup(studyGroup);

       AddStudyGroupRequest studyGroupRequest = new AddStudyGroupRequest();
       studyGroupRequest.setGroupName(groupName);
       studyGroupRequest.setDiscussionTopic(discussionTopic);
       studyGroupRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));
       studyGroupRequest.setActive(active);

       ResultActions actions = mvc.perform(post("/v1/groups")
               .content(mapper.writeValueAsString(studyGroupRequest))
               .accept(MediaType.APPLICATION_JSON)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is2xxSuccessful());

       String responseBody = actions.andReturn().getResponse().getContentAsString();
        AddStudyGroupResponse response = mapper.readValue(responseBody,AddStudyGroupResponse.class);
        assertThat(response.getGroupId()).isNotEmpty().as("The group id is populated");
        assertThat(response.getGroupName()).isEqualTo(studyGroupRequest.getGroupName()).as("The name is correct");
        assertThat(response.isActive()).isTrue();
    }

    @Test
    public void addStudyGroup_AddingStudyGroupFails() throws Exception {
        String groupName = null;
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        AddStudyGroupRequest studyGroupRequest = new AddStudyGroupRequest();
        studyGroupRequest.setGroupName(groupName);
        studyGroupRequest.setDiscussionTopic(discussionTopic);
        studyGroupRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));
        studyGroupRequest.setActive(active);

       mvc.perform(post("/v1/groups")
                        .content(mapper.writeValueAsString(studyGroupRequest))// rest sets up the http request
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());// action
    }

    @Test
    public void addMemberToAStudyGroup_success() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;
        String memberId = "person1@aol.com";
        String password = "Password1!";

        StudyGroup studyGroup = new StudyGroup(groupId,groupName,discussionTopic,date,active);

        // Add StudyGroup
        studyGroupService.addNewStudyGroup(studyGroup);

        // Add Member
        UserLoginRequest request = getUserLoginRequest(memberId, password);
        userLogInController.registerUser(request);

        // Create studyGroup to member association
        //studyGroupService.addMemberToStudyGroup(studyGroup, memberId);

        ResultActions actions = mvc.perform(post("/v1/groups/{groupId}/members/{memberId}",groupId, memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println(actions.andReturn().getResponse().getContentAsString());


        String responseBody = actions.andReturn().getResponse().getContentAsString();
        StudyGroupMemberResponse response = mapper.readValue(responseBody, StudyGroupMemberResponse.class);
        assertThat(response.getGroupId()).isNotEmpty().as("The ID is populated");
        assertThat(response.getMemberId()).isNotEmpty().as("The ID is populated");
        assertThat(response.getGroupName()).isEqualTo(studyGroup.getGroupName()).as("The name is correct");
        assertThat(response.isActive()).isTrue();
    }


    @NotNull
    private static UserLoginRequest getUserLoginRequest(String memberId, String password) {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(memberId);
        request.setPassword(password);
        return request;
    }

    @Test
    public void addMemberToAStudyGroup_missingMember_Unsuccessful() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;
        String memberId = "person1@aol.com";
        String password = "Password1!";

        StudyGroup studyGroup = new StudyGroup(groupId,groupName,discussionTopic,date,active);

        // Add StudyGroup
        studyGroupService.addNewStudyGroup(studyGroup);


        // Create studyGroup to member association
        //studyGroupService.addMemberToStudyGroup(studyGroup, memberId);

        ResultActions actions = mvc.perform(post("/v1/groups/{groupId}/members/{memberId}",groupId, memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());// action


    }
    @Test
    public void getStudyGroupMembers_success() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;
        String memberId = "person1@aol.com";
        String password = "Password1!";

        // set a group
        StudyGroup studyGroup = new StudyGroup(groupId,groupName,discussionTopic,date,active);
        studyGroupService.addNewStudyGroup(studyGroup);

        // list of members
        UserLoginRequest request = getUserLoginRequest(memberId, password);
        userLogInController.registerUser(request);


    }


}







