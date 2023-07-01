package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.repositories.model.StudyGroupReviewId;
import com.kenzie.appserver.service.StudyGroupReviewService;
import com.kenzie.appserver.service.StudyGroupService;
import com.kenzie.appserver.service.model.StudyGroupReview;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class StudyGroupReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudyGroupReviewService reviewService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    /** ------------------------------------------------------------------------
     *  Submit review
     *  ------------------------------------------------------------------------

     * Acceptance criteria: a new review for the study group is submitted
     * Endpoint(s) tested: "/v1//studygroup/reviews"
     * GIVEN (Preconditions): a review for study group is submitted
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 200 code, groupId, reviewId and topic are populated
     * Clean up : restore the state of system back to original state
     */

    @Test
    public void submitReview_successful() throws Exception {
        String groupId = "21";
        String groupName = "Group21";
        String discussionTopic = "java";
        double rating = 5.0;
        String comment = "Very helpful";

        StudyGroupReviewRequest request = new StudyGroupReviewRequest();
        request.setGroupId(groupId);
        request.setGroupName(groupName);
        request.setDiscussionTopic(discussionTopic);
        request.setRating(rating);
        request.setReviewComment(comment);


        ResultActions actions = mvc.perform(post("/v1//studygroup/reviews")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        StudyGroupReviewResponse response = mapper.readValue(responseBody, StudyGroupReviewResponse.class);
        assertThat(response.getGroupId()).isNotEmpty().as("The group id is populated");
        assertThat(response.getReviewId()).isNotEmpty().as("The review id is populated");
        assertThat(response.getDiscussionTopic()).isEqualTo(request.getDiscussionTopic()).as("The topic is correct");
        assertThat(response.getRating()).isEqualTo(request.getRating()).as("The rating is correct");
    }

    /**
    * Acceptance criteria: a new review for the study group is not submitted
     * Endpoint(s) tested: "/v1//studygroup/reviews"
     * GIVEN (Preconditions): a review for study group is not submitted
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 400 error code
     *
     */

    @Test
    public void submitReview_unsuccessful() throws Exception {
        String groupId = null;
        String groupName = "Group21";
        String discussionTopic = "java";
        double rating = 5.0;
        String comment = "Very helpful";

        StudyGroupReviewRequest request = new StudyGroupReviewRequest();
        request.setGroupId(groupId);
        request.setGroupName(groupName);
        request.setDiscussionTopic(discussionTopic);
        request.setRating(rating);
        request.setReviewComment(comment);


        ResultActions actions = mvc.perform(post("/v1/studygroup/reviews")
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /** ------------------------------------------------------------------------
     *  Get reviews by Topic
     *  ------------------------------------------------------------------------

     * Acceptance criteria: find reviews of Group Study By Topic
     * Endpoint(s) tested: "/v1"/studygroup/reviews/discussionTopic/{discussionTopic}"
     * GIVEN (Preconditions): a study group and its reviews are added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 200 code, groupId, reviewId and topic are populated
     * Clean up: tear down the created study group set up and restore the state
     */

    @Test
    public void getStudyGroupReviewsByTopic_successful() throws Exception {
        String groupId1 = "21";
        String reviewId1 = UUID.randomUUID().toString();
        String groupName1 = "Group21";
        String discussionTopic = "datastructures";
        double rating1 = 5.0;
        String comment1 = "Very helpful";
        String groupId2 = "22";
        String reviewId2 = UUID.randomUUID().toString();
        String groupName2 = "Group22";
        double rating2 = 2.0;
        String comment2 = "Blah!";

        List<StudyGroupReview> reviewList = new ArrayList<>();
        StudyGroupReview reviewByTopic1 = new StudyGroupReview(groupId1, groupName1, reviewId1, discussionTopic, rating1, 5.0, comment1);
        reviewList.add(reviewByTopic1);
        StudyGroupReview reviewByTopic2 = new StudyGroupReview(groupId2, groupName2, reviewId2, discussionTopic, rating2, 2.0, comment2);
        reviewList.add(reviewByTopic2);

        for(StudyGroupReview review: reviewList){
            reviewService.submitStudyGroupReview(review);
        }

        ResultActions actions = mvc.perform(get("/v1/studygroup/reviews/discussionTopic/{discussionTopic}", discussionTopic)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);
        List<GroupReviewResponse> responseList = mapper.readValue(responseBody, new TypeReference<>() {});

        assertThat(responseList).isNotEmpty().as("Response list is not empty");
        assertTrue(responseList.size() == 2);

        for(GroupReviewResponse response: responseList) {
            if(response.getGroupId().equals(groupId1)) {
                assertThat(response.getReviewId()).isEqualTo(reviewId1);
                assertThat(response.getGroupName()).isEqualTo(groupName1);
                assertThat(response.getDiscussionTopic()).isEqualTo(discussionTopic);
            }else if(response.getGroupId().equals(groupId2)){
                assertThat(response.getReviewId()).isEqualTo(reviewId2);
                assertThat(response.getGroupName()).isEqualTo(groupName2);
                assertThat(response.getDiscussionTopic()).isEqualTo(discussionTopic);
            }else{
                Assertions.assertTrue(false);
            }
        }
    }



}
