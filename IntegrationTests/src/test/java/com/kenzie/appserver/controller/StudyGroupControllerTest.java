package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.AddStudyGroupRequest;
import com.kenzie.appserver.controller.model.AddStudyGroupResponse;
import com.kenzie.appserver.service.StudyGroupService;
import com.kenzie.appserver.service.model.StudyGroup;
import net.andreinc.mockneat.MockNeat;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.andreinc.mockneat.MockNeat;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class StudyGroupControllerTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        StudyGroupService studyGroupService;

        private final MockNeat mockNeat = MockNeat.threadLocal();

        private static final ObjectMapper mapper = new ObjectMapper();

        @BeforeAll
        public static void setup() {
                mapper.registerModule(new Jdk8Module());
        }

        @Test
        public void addStudyGroup_StudyGroupAdded() throws Exception {
                String groupName = "Group1";
                String discussionTopic = "Discussion Topic";
                ZonedDateTime date = ZonedDateTime.now();
                boolean active = false;

                AddStudyGroupRequest addStudyGroupRequest = new AddStudyGroupRequest();
                addStudyGroupRequest.setGroupName(groupName);
                addStudyGroupRequest.setDiscussionTopic(discussionTopic);
                addStudyGroupRequest.setCreationDate(date);
                addStudyGroupRequest.setActive(active);

                ResultActions actions = mvc.perform(post("/customers/")
                                .content(mapper.writeValueAsString(addStudyGroupRequest))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful());

                String responseBody = actions.andReturn().getResponse().getContentAsString();
                AddStudyGroupResponse response = mapper.readValue(responseBody, AddStudyGroupResponse.class);
                assertThat(response.getGroupId()).isNotEmpty().as("The ID is populated");
                assertThat(response.getGroupName()).isEqualTo(addStudyGroupRequest.getGroupName()).as("The name is correct");
                assertThat(response.getCreationDate()).isAfterOrEqualTo(date).as("The date is populated");
                assertThat(response.isActive()).isTrue();
        }

}


