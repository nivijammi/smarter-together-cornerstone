package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import com.kenzie.appserver.service.StudyGroupService;
import com.kenzie.appserver.service.model.Member;
import com.kenzie.appserver.service.model.StudyGroup;
import net.andreinc.mockneat.MockNeat;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


    /** ------------------------------------------------------------------------
     *  Add Study Group
     *  ------------------------------------------------------------------------

     * Acceptance criteria: a new study group is added
     * Endpoint(s) tested: "/v1/groups/{groupId}"
     * GIVEN (Preconditions): a study group is added
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 200 code, groupId, groupName is not empty
     * Clean up : restore the state of system back to original state
     */

    @Test
    public void addStudyGroup_addsAStudyGroup() throws Exception {

        String groupId = UUID.randomUUID().toString();
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        //StudyGroup studyGroup= new StudyGroup(groupId, groupName, discussionTopic, date, active);

        //studyGroupService.addNewStudyGroup(studyGroup);

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
        AddStudyGroupResponse response = mapper.readValue(responseBody, AddStudyGroupResponse.class);
        assertThat(response.getGroupId()).isNotEmpty().as("The group id is populated");
        assertThat(response.getGroupName()).isEqualTo(studyGroupRequest.getGroupName()).as("The name is correct");
        assertThat(response.isActive()).isTrue();

        //studyGroupService.deleteStudyGroup(groupId);
    }

    /**
     * Acceptance criteria: a new study group is not added
     * Endpoint(s) tested: "/v1/groups/{groupId}"
     * GIVEN (Preconditions): a study group not is added
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 400 error
     * Clean up : none, study group not created
     */
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

    /** ------------------------------------------------------------------------
     *  Add Member To Study Group
     *  ------------------------------------------------------------------------

     * Acceptance criteria: a new member is added to study group
     * Endpoint(s) tested: "/v1"/groups/{groupId}/members/{memberId}"
     * GIVEN (Preconditions): a study group is added, member is added
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 200, groupId, memberId is populated
     * Clean up: tear down the created study group set up and restore the state
     */

    @Test
    public void addMemberToAStudyGroup_success() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;
        String memberEmailPrefix = mockNeat.strings().valStr();
        String memberId = memberEmailPrefix + "@" + "aol.com";
        String password = "Password1!";

        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);

        // Add StudyGroup
        studyGroupService.addNewStudyGroup(studyGroup);

        // Add Member
        //UserLoginRequest request = getUserLoginRequest(memberId, password);
        //userLogInController.registerUser(request);
        StudyGroupMemberRequest request = new StudyGroupMemberRequest();
        request.setGroupId(groupId);
        request.setGroupName(groupName);
        request.setDiscussionTopic(discussionTopic);
        request.setCreationDate(String.valueOf(date));
        request.setActive(active);

        ResultActions actions = mvc.perform(post("/v1/groups/{groupId}/members/{memberId}", groupId, memberId)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println(actions.andReturn().getResponse().getContentAsString());


        String responseBody = actions.andReturn().getResponse().getContentAsString();
        StudyGroupMemberResponse response = mapper.readValue(responseBody, StudyGroupMemberResponse.class);
        assertThat(response.getGroupId()).isNotEmpty().as("The group id is populated");
        assertThat(response.getMemberId()).isNotEmpty().as("The member id is populated");
        assertThat(response.getGroupName()).isEqualTo(studyGroup.getGroupName()).as("The name is correct");
        assertThat(response.isActive()).isTrue();

        studyGroupService.deleteStudyGroup(groupId);

    }

    // helper method
    @NotNull
    private static UserLoginRequest getUserLoginRequest(String memberId, String password) {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(memberId);
        request.setPassword(password);
        return request;
    }

    /**
     * Acceptance criteria: a new member is not added to study group
     * Endpoint(s) tested: "/v1"/groups/{groupId}/members/{memberId}"
     * GIVEN (Preconditions): a study group is added, member is missing
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 400 error
     *
     */
    @Test
    public void addMemberToAStudyGroup_missingMember_unsuccessful() throws Exception {
        String groupId = "1";
        String memberId = null;

        mvc.perform(post("/v1/groups/{groupId}/members/{memberId}", groupId, memberId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());// action


    }
    /** ------------------------------------------------------------------------
     *  Find Study Group Members
     *  ------------------------------------------------------------------------
     *
     * Acceptance criteria: get all members in a group
     * Endpoint(s) tested: "/v1"/groups/{groupId}/members/"
     * GIVEN (Preconditions): a study group is added, list of members is added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 200, groupId, memberId in list are populated
     * Clean up : restore the state of system back to original state
     */

    @Test
    public void getStudyGroupMembers_success() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        String memberId1 = "person1@aol.com";
        String password1 = "Password1!";

        String memberId2 = "person2@aol.com";
        String password2 = "Password2!";

        // set a group
        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        studyGroupService.addNewStudyGroup(studyGroup);

        // list of members
        List<String> memberList = new ArrayList<>();
        UserLoginRequest request1 = getUserLoginRequest(memberId1, password1);
        userLogInController.registerUser(request1);
        memberList.add(memberId1);

        UserLoginRequest request2 = getUserLoginRequest(memberId2, password2);
        userLogInController.registerUser(request2);
        memberList.add(memberId2);

        for (String member : memberList) {
            studyGroupService.addMemberToStudyGroup(studyGroup, member);
        }

        // when
        StudyGroupMemberRequest studyGroupMemberRequest = new StudyGroupMemberRequest();
        studyGroupMemberRequest.setGroupId(groupId);
        studyGroupMemberRequest.setMemberId(memberId1);
        studyGroupMemberRequest.setGroupName(groupName);
        studyGroupMemberRequest.setDiscussionTopic(discussionTopic);
        studyGroupMemberRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));
        studyGroupMemberRequest.setActive(false);

        ResultActions actions = mvc.perform(get("/v1/groups/{groupId}/members/", groupId)
                        // create a request
                        .content(mapper.writeValueAsString(studyGroupMemberRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());


        // then

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);
        List<StudyGroupMemberResponse> responseList = mapper.readValue(responseBody, new TypeReference<>() {
        });

        assertThat(responseList).isNotEmpty().as("Response list is not empty");
        StudyGroupMemberResponse response1 = responseList.get(0);
        assertThat(response1.getGroupId()).isEqualTo(groupId);
        assertThat(response1.getMemberId()).isEqualTo(memberId1);
        assertThat(response1.getGroupName()).isEqualTo(groupName);
        assertThat(response1.getDiscussionTopic()).isEqualTo(discussionTopic);


        StudyGroupMemberResponse response2 = responseList.get(1);
        assertThat(response2.getGroupId()).isEqualTo(groupId);
        assertThat(response2.getMemberId()).isEqualTo(memberId2);
        assertThat(response2.getGroupName()).isEqualTo(groupName);
        assertThat(response2.getDiscussionTopic()).isEqualTo(discussionTopic);

        studyGroupService.deleteStudyGroup(groupId);

    }
    /**
     * Acceptance criteria: unable to get all members in a group
     * Endpoint(s) tested: "/v1"/groups/{groupId}/members/"
     * GIVEN (Preconditions): a study group is added, list of members not added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 400 error code
     */

    @Test
    public void getStudyGroupMembers_unsuccessful() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        String memberId1 = "person1@aol.com";
        String password1 = "Password1!";

        String memberId2 = "person2@aol.com";
        String password2 = "Password2!";

        // set a group
        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        studyGroupService.addNewStudyGroup(studyGroup);

        // list of members - no members present
//        List<String> memberList = new ArrayList<>();
//        for (String member : memberList) {
//            studyGroupService.addMemberToStudyGroup(studyGroup, member);
//        }

        // when
        StudyGroupMemberRequest studyGroupMemberRequest = new StudyGroupMemberRequest();
        studyGroupMemberRequest.setGroupId(groupId);
        studyGroupMemberRequest.setMemberId(memberId1);
        studyGroupMemberRequest.setGroupName(groupName);
        studyGroupMemberRequest.setDiscussionTopic(discussionTopic);
        studyGroupMemberRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));
        studyGroupMemberRequest.setActive(false);

        ResultActions actions = mvc.perform(get("/v1/groups/{groupId}/members/", groupId)
                        // create a request
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /** ------------------------------------------------------------------------
     *  Delete A Member From Study Group
     *  ------------------------------------------------------------------------

     * Acceptance criteria: removes member from group
     * Endpoint(s) tested: "/v1/groups/{groupId}/members/{memberId}"
     * GIVEN (Preconditions): a study group is added/ member is added/ get the member
     * WHEN (Action(s)): delete request
     * THEN (Verification steps): 200, the member is not found in group
     */
    @Test
    public void removeMemberFromStudyGroup_deleteSuccessful() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        String memberId1 = "person1@aol.com";
        String password1 = "Password1!";

        String memberId2 = "person2@aol.com";
        String password2 = "Password2!";

        // set a group
        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        studyGroupService.addNewStudyGroup(studyGroup);

        // list of members
        List<String> memberList = new ArrayList<>();
        UserLoginRequest request1 = getUserLoginRequest(memberId1, password1);
        userLogInController.registerUser(request1);
        memberList.add(memberId1);

        UserLoginRequest request2 = getUserLoginRequest(memberId2, password2);
        userLogInController.registerUser(request2);
        memberList.add(memberId2);

        for (String member : memberList) {
            studyGroupService.addMemberToStudyGroup(studyGroup, member);
        }

        // WHEN
        mvc.perform(delete("/v1/groups/{groupId}/members/{memberId}", groupId,memberId1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        //THEN
        mvc.perform(get("/v1/groups/{groupId}/members/{memberId}", groupId,memberId1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Member member = studyGroupService.findMemberById(memberId2);
        assertThat(member.getEmail()).isEqualTo(memberId2);

    }

    /**
     * Acceptance criteria: removes member from group
     * Endpoint(s) tested: "/v1/groups/{groupId}/members/{memberId}"
     * GIVEN (Preconditions): a study group is added/ member is added/ get the member
     * WHEN (Action(s)): delete request
     * THEN (Verification steps): 200, the member is not found in group
     */
    @Test
    public void removeMemberFromStudyGroup_deleteUnSuccessful() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        String memberId1 = "person2@aol.com";
        String password1 = "Password1!";

        String memberId2 = "person2@aol.com";
        String password2 = "Password2!";

        // set a group
        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        studyGroupService.addNewStudyGroup(studyGroup);


        // WHEN
        mvc.perform(delete("/v1/groups/{groupId}/members/{memberId}", groupId,memberId1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    /** ------------------------------------------------------------------------
     *  Delete All Members From Study Group
     *  ------------------------------------------------------------------------

     * Acceptance criteria: removes all member from group
     * Endpoint(s) tested: "/v1/groups/{groupId}/members/"
     * GIVEN (Preconditions): a study group is added/ members are added/
     * WHEN (Action(s)): delete request
     * THEN (Verification steps): 200, the members are not found in group
     */
    @Test
    public void removeAllMemberFromStudyGroup_deleteSuccessful() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        String memberId1 = "person1@aol.com";
        String password1 = "Password1!";

        String memberId2 = "person2@aol.com";
        String password2 = "Password2!";

        // set a group
        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        studyGroupService.addNewStudyGroup(studyGroup);

        // list of members
        List<String> memberList = new ArrayList<>();
        UserLoginRequest request1 = getUserLoginRequest(memberId1, password1);
        userLogInController.registerUser(request1);
        memberList.add(memberId1);

        UserLoginRequest request2 = getUserLoginRequest(memberId2, password2);
        userLogInController.registerUser(request2);
        memberList.add(memberId2);

        for (String member : memberList) {
            studyGroupService.addMemberToStudyGroup(studyGroup, member);
        }

        // WHEN
        mvc.perform(delete("/v1/groups/{groupId}/members/", groupId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        //THEN
        mvc.perform(get("/v1/groups/{groupId}/members/", groupId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Acceptance criteria: does not remove all members from group
     * Endpoint(s) tested: "/v1/groups/{groupId}/members/"
     * GIVEN (Preconditions): a study group is added/ one member is added/
     * WHEN (Action(s)): delete request
     * THEN (Verification steps): 400, the member is found in group
     */
    @Test
    public void removeMemberFromStudyGroup_notAllRemoved() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        String memberId1 = "person1@aol.com";
        String password1 = "Password1!";

        String memberId2 = "person2@aol.com";
        String password2 = "Password2!";

        // set a group
        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);
        studyGroupService.addNewStudyGroup(studyGroup);

        // list of members
        List<String> memberList = new ArrayList<>();
        UserLoginRequest request1 = getUserLoginRequest(memberId2, password2);
        userLogInController.registerUser(request1);

        UserLoginRequest request2 = getUserLoginRequest(memberId2, password2);
        userLogInController.registerUser(request2);
        memberList.add(memberId2);

        for (String member : memberList) {
            studyGroupService.addMemberToStudyGroup(studyGroup, member);
        }

        // WHEN
        mvc.perform(delete("/v1/groups/{groupId}/members/", groupId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        //THEN
        mvc.perform(get("/v1/groups/{groupId}/members/{memberId}", groupId,memberId1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Member member = studyGroupService.findMemberById(memberId2);
        assertThat(member.getEmail()).isEqualTo(memberId2);

    }
    /** ------------------------------------------------------------------------
     *  Get Study Group by id
     *  ------------------------------------------------------------------------

     * Acceptance criteria: finds study group by id
     * Endpoint(s) tested: "/v1/groups/{groupId}/"
     * GIVEN (Preconditions): a study group is added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 200, the study group is found
     * Clean up : restore the state of system back to original state
     */
    @Test
    public void getStudyGroupById_successful() throws Exception {
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;
        String memberId = "person1@aol.com";
        String password = "Password1!";

        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);

        // Add StudyGroup
        studyGroupService.addNewStudyGroup(studyGroup);

        //THEN
        ResultActions actions = mvc.perform(get("/v1/groups/{groupId}", groupId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        AddStudyGroupResponse response = mapper.readValue(responseBody, AddStudyGroupResponse.class);
        assertThat(response.getGroupId()).isNotEmpty().as("The group id is populated");
        assertThat(response.getGroupName()).isNotEmpty().as("The name is correct");
        assertThat(response.isActive()).isTrue();

        studyGroupService.deleteStudyGroup(groupId);
    }
    /**
     * Acceptance criteria: finds no study group by id
     * Endpoint(s) tested: "/v1/groups/{groupId}/"
     * GIVEN (Preconditions): study group is not added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 400
     */
    @Test
    public void getStudyGroupById_unsuccessful() throws Exception {
     String groupId = "1";

        //THEN
        mvc.perform(get("/v1/groups/{groupId}", groupId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    /** ------------------------------------------------------------------------
     *  Get All Study Groups
     *  ------------------------------------------------------------------------

     * Acceptance criteria: find all study groups
     * Endpoint(s) tested: "/v1/groups/"
     * GIVEN (Preconditions): study group is added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 200, group id, name and other are populated
     */
    @Test
    public void getAllStudyGroups_Successful() throws Exception {
        String groupId1= "1";
        String groupId2= "2";
        String groupName1 = "groupName1";
        String groupName2 = "groupName2";
        String discussionTopic1 = "discussionTopic1";
        String discussionTopic2 = "discussionTopic2";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        List<StudyGroup> groups = new ArrayList<>();
        StudyGroup studyGroup1 = new StudyGroup(groupId1, groupName1, discussionTopic1, date, active);
        groups.add(studyGroup1);
        StudyGroup studyGroup2 = new StudyGroup(groupId2, groupName2, discussionTopic2, date, active);
        groups.add(studyGroup2);

        for(StudyGroup group : groups){
            studyGroupService.addNewStudyGroup(group);
        }

        //WHEN
        ResultActions actions = mvc.perform(get("/v1/groups/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);
        List<AddStudyGroupResponse> responses = mapper.readValue(responseBody, new TypeReference<>() {
        });

            assertThat(responses.size()).isGreaterThan(0).as("There are responses");
            for(AddStudyGroupResponse response:responses){
                assertThat(response.getGroupId()).isNotEmpty().as("The ID is populated");
                assertThat(response.getGroupName()).isNotEmpty().as("The name is populated");
        }
    }

    /** ------------------------------------------------------------------------
     *  Update Study Group
     *  ------------------------------------------------------------------------

     * Acceptance criteria: updates the study group
     * Endpoint(s) tested: "/v1/groups/{groupId}"
     * GIVEN (Preconditions): study group is added
     * WHEN (Action(s)): put request
     * THEN (Verification steps): 200, group id, name and other are populated
     */

    @Test
    public void updateStudyGroup_success() throws Exception{
        String groupId = "1";
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);

        studyGroupService.addNewStudyGroup(studyGroup);

        AddStudyGroupRequest studyGroupRequest = new AddStudyGroupRequest();
        studyGroupRequest.setGroupName(groupName);
        studyGroupRequest.setDiscussionTopic(discussionTopic);
        studyGroupRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));
        studyGroupRequest.setActive(true);

        ResultActions actions = mvc.perform(put("/v1/groups/{groupId}",groupId)
                        .content(mapper.writeValueAsString(studyGroupRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        AddStudyGroupResponse response = mapper.readValue(responseBody, AddStudyGroupResponse.class);
        assertThat(response.getGroupId()).isEqualTo(groupId).as("The group id is populated");
        assertThat(response.getGroupName()).isEqualTo(studyGroupRequest.getGroupName()).as("The name is correct");
        assertThat(response.isActive()).isTrue();

        studyGroupService.deleteStudyGroup(groupId);
    }

    /**
     * Acceptance criteria: update not successful
     * Endpoint(s) tested: "/v1/groups/{groupId}"
     * GIVEN (Preconditions): study group is added
     * WHEN (Action(s)): put request
     * THEN (Verification steps): 200, group id, name and other are populated
     */

    @Test
    public void updateStudyGroup_unsuccessful() throws Exception{

        AddStudyGroupRequest studyGroupRequest = new AddStudyGroupRequest();
        studyGroupRequest.setGroupName("groupName");
        studyGroupRequest.setDiscussionTopic("discussionTopic");
        studyGroupRequest.setCreationDate(new ZonedDateTimeConverter().convert(ZonedDateTime.now()));
        studyGroupRequest.setActive(true);

        String groupId = "1";
        mvc.perform(put("/v1/groups/{groupId}",groupId)
                        .content(mapper.writeValueAsString(studyGroupRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    /** ------------------------------------------------------------------------
     *  Delete Study Group
     *  ------------------------------------------------------------------------

     * Acceptance criteria: deletes study group
     * Endpoint(s) tested: "/v1/groups/{groupId}"
     * GIVEN (Preconditions): study group is added
     * WHEN (Action(s)): delete request
     * THEN (Verification steps): 200, no study group with id exists
     */

    @Test
    public void deleteCustomer_success() throws Exception {
        String groupId = UUID.randomUUID().toString();
        String groupName = mockNeat.strings().valStr();
        String discussionTopic = "discussionTopic";
        ZonedDateTime date = ZonedDateTime.now();
        boolean active = false;

        StudyGroup studyGroup = new StudyGroup(groupId, groupName, discussionTopic, date, active);

        studyGroupService.addNewStudyGroup(studyGroup);

        AddStudyGroupRequest studyGroupRequest = new AddStudyGroupRequest();
        studyGroupRequest.setGroupName(groupName);
        studyGroupRequest.setDiscussionTopic(discussionTopic);
        studyGroupRequest.setCreationDate(new ZonedDateTimeConverter().convert(date));
        studyGroupRequest.setActive(active);

        // WHEN
        mvc.perform(delete("/v1/groups/{groupId}",groupId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        // THEN
        mvc.perform(get("/v1/groups/{groupId}", groupId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    /**
     * Acceptance criteria:  deletes study group unsuccessful
     * Endpoint(s) tested: "/v1/groups/{groupId}"
     * GIVEN (Preconditions): study group is added
     * WHEN (Action(s)): delete request
     * THEN (Verification steps): 400
     */

    @Test
    public void deleteCustomer_unsuccessful() throws Exception {

        String groupId = "1";

        mvc.perform(delete("/v1/groups/{groupId}", groupId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

}













