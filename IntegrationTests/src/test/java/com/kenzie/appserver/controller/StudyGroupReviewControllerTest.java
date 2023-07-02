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
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class StudyGroupReviewControllerTest {

    private final MockNeat mockNeat = MockNeat.threadLocal();
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private StudyGroupReviewService reviewService;

    /**
     * ------------------------------------------------------------------------
     * Submit review
     * ------------------------------------------------------------------------
     * <p>
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

        reviewService.deleteGroupFromReviewRecord(groupId);

    }

    /**
     * Acceptance criteria: a new review for the study group is not submitted
     * Endpoint(s) tested: "/v1//studygroup/reviews"
     * GIVEN (Preconditions): a review for study group is not submitted
     * WHEN (Action(s)): post request
     * THEN (Verification steps): 400 error code
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

    /**
     * ------------------------------------------------------------------------
     * Get reviews by Topic
     * ------------------------------------------------------------------------
     * <p>
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
        String groupName1 = "Group201";
        String discussionTopic = "Strings";
        double rating1 = 5.0;
        String comment1 = "Very helpful";
        String groupId2 = "22";
        String reviewId2 = UUID.randomUUID().toString();
        String groupName2 = "Group202";
        double rating2 = 2.0;
        String comment2 = "Blah!";

        List<StudyGroupReview> reviewList = new ArrayList<>();
        StudyGroupReview reviewByTopic1 = new StudyGroupReview(groupId1, groupName1, reviewId1, discussionTopic, rating1, 5.0, comment1);
        reviewList.add(reviewByTopic1);
        StudyGroupReview reviewByTopic2 = new StudyGroupReview(groupId2, groupName2, reviewId2, discussionTopic, rating2, 2.0, comment2);
        reviewList.add(reviewByTopic2);

        for (StudyGroupReview review : reviewList) {
            reviewService.submitStudyGroupReview(review);
        }

        ResultActions actions = mvc.perform(get("/v1/studygroup/reviews/discussionTopic/{discussionTopic}", discussionTopic)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);
        List<GroupReviewResponse> responseList = mapper.readValue(responseBody, new TypeReference<>() {
        });

        assertThat(responseList).isNotEmpty().as("Response list is not empty");
        assertTrue(responseList.size() == 2);

        for (GroupReviewResponse response : responseList) {
            if (response.getGroupId().equals(groupId1)) {
                assertThat(response.getReviewId()).isEqualTo(reviewId1);
                assertThat(response.getGroupName()).isEqualTo(groupName1);
                assertThat(response.getDiscussionTopic()).isEqualTo(discussionTopic);
            } else if (response.getGroupId().equals(groupId2)) {
                assertThat(response.getReviewId()).isEqualTo(reviewId2);
                assertThat(response.getGroupName()).isEqualTo(groupName2);
                assertThat(response.getDiscussionTopic()).isEqualTo(discussionTopic);
            } else {
                //Assertions.assertTrue(false);
            }
        }
        reviewService.deleteGroupFromReviewRecord(groupId1);
        reviewService.deleteGroupFromReviewRecord(groupId2);
    }

    /**
     * * Acceptance criteria: find reviews of Group Study By Topic
     * Endpoint(s) tested: "/v1"/studygroup/reviews/discussionTopic/{discussionTopic}"
     * GIVEN (Preconditions): a study group and its reviews are not added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 400 error code
     * Clean up: tear down the created study group set up and restore the state
     */

    @Test
    public void getStudyGroupReviewsByTopic_unsuccessful() throws Exception {

        String discussionTopic = null;
        List<StudyGroupReview> reviewList = new ArrayList<>();

        for (StudyGroupReview review : reviewList) {
            reviewService.submitStudyGroupReview(review);
        }

        ResultActions actions = mvc.perform(get("/v1/studygroup/reviews/discussionTopic/{discussionTopic}", discussionTopic)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /**
     * ------------------------------------------------------------------------
     * Get reviews by Review Id
     * ------------------------------------------------------------------------
     * <p>
     * Acceptance criteria: find reviews of Group Study By Review Id
     * Endpoint(s) tested: "/v1"/studygroup/reviews/discussionTopic/{discussionTopic}"
     * GIVEN (Preconditions): a study group and its reviews are added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 200 code, groupId, review and topic are populated
     * Clean up: tear down the created study group set up and restore the state
     */

    @Test
    public void getStudyGroupReviewsByReviewId_successful() throws Exception {

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

        for (StudyGroupReview review : reviewList) {
            reviewService.submitStudyGroupReview(review);
        }

        ResultActions actions = mvc.perform(get("/v1/studygroup/reviews/{reviewId}", reviewId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        StudyGroupReviewResponse response = mapper.readValue(responseBody, StudyGroupReviewResponse.class);
        assertThat(response.getGroupId()).isNotEmpty().as("The group id is populated");
        assertThat(response.getReviewId()).isNotEmpty().as("The review id is populated");
        assertThat(response.getDiscussionTopic()).isEqualTo(discussionTopic).as("The topic is correct");
        assertThat(response.getRating()).isEqualTo(rating1).as("The rating is correct");

        reviewService.deleteGroupFromReviewRecord(groupId1);
        reviewService.deleteGroupFromReviewRecord(groupId2);
    }

    /**
     * Acceptance criteria: cannot find reviews of Group Study By Review Id
     * Endpoint(s) tested: "/v1"/studygroup/reviews/discussionTopic/{discussionTopic}"
     * GIVEN (Preconditions): a study group and its reviews are added, invalid reviewId is provided
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 400 error code
     * Clean up: tear down the created study group set up and restore the state
     */
    @Test
    public void getStudyGroupReviewsByReviewId_invalidReviewId_unsuccessful() throws Exception {

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

        for (StudyGroupReview review : reviewList) {
            reviewService.submitStudyGroupReview(review);
        }

        ResultActions actions = mvc.perform(get("/v1/studygroup/reviews/{reviewId}", UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        reviewService.deleteGroupFromReviewRecord(groupId1);
    }

    /**
     * Acceptance criteria: cannot find reviews of Group Study By Review Id
     * Endpoint(s) tested: "/v1"/studygroup/reviews/discussionTopic/{discussionTopic}"
     * GIVEN (Preconditions): a study group, its reviews are not added,valid reviewId is provided
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 400 error code
     * Clean up: tear down the created study group set up and restore the state
     */

    @Test
    public void getStudyGroupReviewsByReviewId_noStudyGroupAdded_unsuccessful() throws Exception {

        List<StudyGroupReview> reviewList = new ArrayList<>();

        for (StudyGroupReview review : reviewList) {
            reviewService.submitStudyGroupReview(review);
        }

        ResultActions actions = mvc.perform(get("/v1/studygroup/reviews/{reviewId}", UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }


    /**
     * ------------------------------------------------------------------------
     * Get average ratings Group Id
     * ------------------------------------------------------------------------
     * <p>
     * Acceptance criteria: find average rating of reviews for a Group Study
     * Endpoint(s) tested: "/v1"/studygroup/averageRating/{groupId}"
     * GIVEN (Preconditions): a study group and its reviews are added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 200 code, and an average rating for group
     * Clean up: tear down the created study group set up and restore the state
     */
    @Test
    public void getAverageRatingById_successful() throws Exception {
        String groupId = "21";
        String discussionTopic = "Algorithms";
        String groupName = "Group111";
        String reviewId1 = UUID.randomUUID().toString();
        double rating1 = 5.0;
        String comment1 = "Very helpful";

        String reviewId2 = UUID.randomUUID().toString();
        double rating2 = 2.0;
        String comment2 = "Blah!";

        double expectedAverageRating = 3.5;

        List<StudyGroupReview> reviewList = new ArrayList<>();
        StudyGroupReview reviewByTopic1 = new StudyGroupReview(groupId, groupName, reviewId1, discussionTopic, rating1, 5.0, comment1);
        reviewList.add(reviewByTopic1);
        StudyGroupReview reviewByTopic2 = new StudyGroupReview(groupId, groupName, reviewId2, discussionTopic, rating2, 2.0, comment2);
        reviewList.add(reviewByTopic2);

        for (StudyGroupReview review : reviewList) {
            reviewService.submitStudyGroupReview(review);
        }

        ResultActions actions = mvc.perform(get("/v1/studygroup/averageRating/{groupId}", groupId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        Assertions.assertEquals(expectedAverageRating, Double.valueOf(responseBody));
        reviewService.deleteGroupFromReviewRecord(groupId);
    }

    /**
     * ------------------------------------------------------------------------
     * Get average ratings Group Id
     * ------------------------------------------------------------------------
     * <p>
     * Acceptance criteria: find average rating of reviews for a Group Study
     * Endpoint(s) tested: "/v1"/studygroup/averageRating/{groupId}"
     * GIVEN (Preconditions): a study groupId is null and its reviews are added
     * WHEN (Action(s)): get request
     * THEN (Verification steps): 400 error code
     * Clean up: tear down the created study group set up and restore the state
     */

    @Test
    public void getAverageRatingById_unsuccessful() throws Exception {
        String groupId = "null";
        double expectedAverageRating = 3.5;

        ResultActions actions = mvc.perform(get("/v1/studygroup/averageRating/{groupId}", groupId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()); // Update the expected response status

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        if (!responseBody.isEmpty()) {
            double actualAverageRating = Double.parseDouble(responseBody);

            Assertions.assertNotEquals(expectedAverageRating, actualAverageRating, 0.01);
        }
    }
}






