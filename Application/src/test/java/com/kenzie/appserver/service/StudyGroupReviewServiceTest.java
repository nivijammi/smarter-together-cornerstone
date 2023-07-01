package com.kenzie.appserver.service;

import com.kenzie.appserver.exception.ReviewNotFoundException;
import com.kenzie.appserver.repositories.StudyGroupReviewRepository;
import com.kenzie.appserver.repositories.model.StudyGroupReviewId;
import com.kenzie.appserver.repositories.model.StudyGroupReviewRecord;
import com.kenzie.appserver.service.model.StudyGroupReview;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudyGroupReviewServiceTest {

    private StudyGroupReviewRepository reviewRepository;
    private StudyGroupReviewService subject;

    @BeforeEach
    void setup(){
        reviewRepository = mock(StudyGroupReviewRepository.class);
        subject = new StudyGroupReviewService(reviewRepository);
    }

    @Test
    public void submitStudyGroupReview_validReview() {
        String groupId = "group123";
        String reviewId = "review123";
        String groupName = "Study Group 123";
        String discussionTopic = "Strings";
        double rating = 4.5;
        double averageRating = 4.5;
        String reviewComments = "A good session";

        StudyGroupReviewRecord savedRecord = new StudyGroupReviewRecord();
        savedRecord.setGroupId(groupId);
        savedRecord.setGroupName(groupName);
        savedRecord.setReviewId(reviewId);
        savedRecord.setDiscussionTopic(discussionTopic);
        savedRecord.setRating(rating);
        savedRecord.setAverageRating(averageRating);
        savedRecord.setReviewComments(reviewComments);

        StudyGroupReview review = new StudyGroupReview(groupId, groupName, reviewId, discussionTopic, rating, rating, reviewComments);
        when(reviewRepository.save(any(StudyGroupReviewRecord.class))).thenReturn(savedRecord);


        StudyGroupReview studyGroupReview = subject.submitStudyGroupReview(review);

        verify(reviewRepository).save(any(StudyGroupReviewRecord.class));
        Assertions.assertEquals(savedRecord.getGroupId(), studyGroupReview.getGroupId());
        Assertions.assertEquals(savedRecord.getGroupName(), studyGroupReview.getGroupName());
        Assertions.assertEquals(savedRecord.getDiscussionTopic(), studyGroupReview.getDiscussionTopic());
        Assertions.assertEquals(savedRecord.getRating(), studyGroupReview.getRating());
        Assertions.assertEquals(savedRecord.getReviewComments(), studyGroupReview.getReviewComments());
    }

    @Test
    void submitStudyGroupReview_NoReview() {
        StudyGroupReview review = new StudyGroupReview();
        assertThrows(ReviewNotFoundException.class, () -> subject.submitStudyGroupReview(review));
    }


    @Test
    void submitStudyGroupReview_nullReview() {
        StudyGroupReview review = null;
        assertThrows(ReviewNotFoundException.class, () -> subject.submitStudyGroupReview(review));
    }

    @Test
    void submitStudyGroupReview_invalidLesserThanRating() {
        String groupId = "group123";
        String reviewId = "review123";
        String groupName = "Study Group 123";
        String discussionTopic = "Strings";
        double rating = 0.5;
        String reviewComments = "A good session";

        StudyGroupReview review = new StudyGroupReview(groupId, groupName, reviewId, discussionTopic, rating, rating, reviewComments);
        assertThrows(ReviewNotFoundException.class, () -> subject.submitStudyGroupReview(review));
    }

    @Test
    void submitStudyGroupReview_invalidGreaterThanRating() {
        String groupId = "group123";
        String reviewId = "review123";
        String groupName = "Study Group 123";
        String discussionTopic = "Strings";
        double rating = 5.4;
        String reviewComments = "A good session";

        StudyGroupReview review = new StudyGroupReview(groupId, groupName, reviewId, discussionTopic, rating, rating, reviewComments);
        assertThrows(ReviewNotFoundException.class, () -> subject.submitStudyGroupReview(review));
    }

    @Test
    void submitStudyGroupReview_emptyComments() {
        String groupId = "group123";
        String reviewId = "review123";
        String groupName = "Study Group 123";
        String discussionTopic = "Strings";
        double rating = 4.5;
        String reviewComments = "";

        StudyGroupReview review = new StudyGroupReview(groupId, groupName, reviewId, discussionTopic, rating, rating, reviewComments);
        assertThrows(ReviewNotFoundException.class, () -> subject.submitStudyGroupReview(review));
    }
    @Test
    void collectRatingsForStudyGroup_returnsExpectedAverageRating() {
        String groupId = "1";
        String reviewId1 = "review123";
        String groupName1 = "group123";
        String discussionTopic1 = "Strings";
        double rating1 = 4.5;
        String reviewComments1= "A good session";
        double averageRating1 = 0.0;

        List<StudyGroupReviewRecord> ratingList = new ArrayList<>();
        StudyGroupReviewId id1 = new StudyGroupReviewId(groupId,reviewId1);
        StudyGroupReviewRecord record1 = new StudyGroupReviewRecord(id1,
                groupName1,discussionTopic1,rating1,reviewComments1,averageRating1);
        ratingList.add(record1);


        String reviewId2 = "review111";
        String groupName2 = "group123";
        String discussionTopic2 = "Strings";
        double rating2 = 3.8;
        String reviewComments2= "I was okay";
        double averageRating2 = 0.0;

        StudyGroupReviewId id2 = new StudyGroupReviewId(groupId,reviewId2);
        StudyGroupReviewRecord record2 = new StudyGroupReviewRecord(id2,
                groupName2,discussionTopic2,rating2,reviewComments2,averageRating2);
        ratingList.add(record2);


        when(reviewRepository.findByGroupId(groupId)).thenReturn(Optional.of(ratingList));

        double totalRating = subject.collectRatingsForStudyGroup(groupId);

        double expectedTotalRating = rating1 + rating2;
        assertEquals(expectedTotalRating, totalRating);

        verify(reviewRepository, times(1)).findByGroupId(groupId);
    }

    @Test
    void collectRatingsForStudyGroup_noRatingsFound() {
        String groupId = "1";

        when(reviewRepository.findByGroupId(groupId)).thenReturn(Optional.empty());

        double totalRating = subject.collectRatingsForStudyGroup(groupId);

        // rating not found
        double expectedTotalRating = 0.0;
        assertEquals(expectedTotalRating, totalRating);

        verify(reviewRepository, times(1)).findByGroupId(groupId);
    }

    @Test
    void collectCommentsForStudyGroup_returnsListOfComments() {
        String groupId = "1";
        String reviewId1 = "review123";
        String groupName1 = "group123";
        String discussionTopic1 = "Strings";
        double rating1 = 4.5;
        String reviewComments1 = "A good session";
        double averageRating1 = 0.0;

        List<StudyGroupReviewRecord> commentList = new ArrayList<>();
        StudyGroupReviewId id1 = new StudyGroupReviewId(groupId, reviewId1);
        StudyGroupReviewRecord record1 = new StudyGroupReviewRecord(id1,
                groupName1, discussionTopic1, rating1, reviewComments1, averageRating1);
        commentList.add(record1);


        String reviewId2 = "review111";
        String groupName2 = "group123";
        String discussionTopic2 = "Strings";
        double rating2 = 3.8;
        String reviewComments2 = "I was okay";
        double averageRating2 = 0.0;

        StudyGroupReviewId id2 = new StudyGroupReviewId(groupId, reviewId2);
        StudyGroupReviewRecord record2 = new StudyGroupReviewRecord(id2,
                groupName2, discussionTopic2, rating2, reviewComments2, averageRating2);
        commentList.add(record2);

        when(reviewRepository.findByGroupId(groupId)).thenReturn(Optional.of(commentList));

        List<String> stringList = subject.collectCommentsForStudyGroup(groupId);

        assertEquals(stringList.size(), 2);
        for (int i = 0; i < stringList.size(); i++) {
            assertEquals(commentList.get(i).getReviewComments(), stringList.get(i));
        }
    }

        @Test
        void collectCommentsForStudyGroup_noCommentsFound() {
            String groupId = "1";

            when(reviewRepository.findByGroupId(groupId)).thenReturn(Optional.empty());

            List<String> stringList = subject.collectCommentsForStudyGroup(groupId);

            assertEquals(stringList.size(),0);


            verify(reviewRepository, times(1)).findByGroupId(groupId);
        }

    @Test
    void calculateAverageRating_returnAverage(){
        String groupId = "1";
        String reviewId1 = "review123";
        String groupName1 = "group123";
        String discussionTopic1 = "Strings";
        double rating1 = 4.5;
        String reviewComments1 = "A good session";
        double averageRating1 = 0.0;

        List<StudyGroupReviewRecord> recordList = new ArrayList<>();
        StudyGroupReviewId id = new StudyGroupReviewId(groupId, reviewId1);
        StudyGroupReviewRecord record1 = new StudyGroupReviewRecord(id,
                groupName1, discussionTopic1, rating1, reviewComments1, averageRating1);
        recordList.add(record1);


        String reviewId2 = "review123";
        String groupName2 = "group123";
        String discussionTopic2 = "Strings";
        double rating2 = 3.8;
        String reviewComments2 = "I was okay";
        double averageRating2 = 0.0;

        StudyGroupReviewId id2 = new StudyGroupReviewId(groupId, reviewId2);
        StudyGroupReviewRecord record2 = new StudyGroupReviewRecord(id,
                groupName2, discussionTopic2, rating2, reviewComments2, averageRating2);
        recordList.add(record2);

        when(reviewRepository.findByGroupId(groupId)).thenReturn(Optional.of(recordList));

        double actualAverageRating = subject.calculateAverageRating(groupId);

        double expectedAverageRating = (rating1 + rating2) / recordList.size();

        assertEquals(actualAverageRating, expectedAverageRating);

        verify(reviewRepository, times(1)).findByGroupId(groupId);


    }

    @Test
    void calculateAverageRating_noRatingsFound() {
        String groupId = "1";
        String reviewId = "review123";

        StudyGroupReviewId id = new StudyGroupReviewId(groupId, reviewId);

        when(reviewRepository.findByGroupId(groupId)).thenReturn(Optional.empty());

        double averageRating = subject.calculateAverageRating(groupId);

        // rating not found
        double expectedAverageRating = 0.0;
        assertEquals(expectedAverageRating, averageRating);

        verify(reviewRepository, times(1)).findByGroupId(groupId);
    }

    @Test
    void getStudyGroupReviewsByTopic_reviewsFound() {
        String discussionTopic = "Strings";
        List<StudyGroupReviewRecord> reviewRecords = new ArrayList<>();

        String groupId = "1";
        String reviewId1 = "review123";
        String groupName1 = "group123";
        String discussionTopic1 = "Strings";
        double rating1 = 4.5;
        String reviewComments1 = "A good session";
        double averageRating1 = 0.0;

        List<StudyGroupReviewRecord> recordList = new ArrayList<>();
        StudyGroupReviewId id = new StudyGroupReviewId(groupId, reviewId1);
        StudyGroupReviewRecord record1 = new StudyGroupReviewRecord(id,
                groupName1, discussionTopic1, rating1, reviewComments1, averageRating1);
        recordList.add(record1);

        String groupId2 = "1";
        String reviewId2 = "review123";
        String groupName2 = "group123";
        String discussionTopic2 = "Strings";
        double rating2 = 3.8;
        String reviewComments2 = "I was okay";
        double averageRating2 = 0.0;

        StudyGroupReviewId id2 = new StudyGroupReviewId(groupId2, reviewId2);
        StudyGroupReviewRecord record2 = new StudyGroupReviewRecord(id2,
                groupName2, discussionTopic2, rating2, reviewComments2, averageRating2);
        recordList.add(record2);

        when(reviewRepository.findByDiscussionTopic(discussionTopic)).thenReturn(Optional.of(recordList));

        List<StudyGroupReview> result = subject.getStudyGroupReviewsByTopic(discussionTopic);

        assertEquals(recordList.size(), result.size());
        assertEquals(groupId, result.get(0).getGroupId());
        assertEquals(reviewId1, result.get(0).getReviewId());
        assertEquals(discussionTopic1, result.get(0).getDiscussionTopic());

        assertEquals(groupId2, result.get(1).getGroupId());
        assertEquals(reviewId2, result.get(1).getReviewId());
        assertEquals(discussionTopic2, result.get(1).getDiscussionTopic());
    }

    @Test
    void getStudyGroupReviewsByTopic_noReviewsFound() {
        String discussionTopic = "Strings";
        when(reviewRepository.findByDiscussionTopic(discussionTopic)).thenReturn(Optional.empty());

        List<StudyGroupReview> studyGroupReviewsByTopic = subject.getStudyGroupReviewsByTopic(discussionTopic);

        assertEquals(Collections.emptyList(), studyGroupReviewsByTopic);
    }

    @Test
    void getStudyGroupReview_reviewFound() {
        String reviewId = "review123";
        String groupId = "group123";
        String groupName = "Study Group 123";
        String discussionTopic = "Strings";
        double rating = 4.5;
        String reviewComments = "A good session";
        double averageRating = 4.5;


        StudyGroupReviewRecord reviewRecord = new StudyGroupReviewRecord();
        reviewRecord.setGroupId(groupId);
        reviewRecord.setGroupName(groupName);
        reviewRecord.setDiscussionTopic(discussionTopic);
        reviewRecord.setRating(rating);
        reviewRecord.setReviewComments(reviewComments);
        reviewRecord.setAverageRating(averageRating);
        when(reviewRepository.findByReviewId(reviewId)).thenReturn(Optional.of(reviewRecord));


        StudyGroupReview groupReview = subject.getStudyGroupReview(reviewId);


        assertNotNull(groupReview);
        assertEquals(groupId, groupReview.getGroupId());
        assertEquals(groupName, groupReview.getGroupName());
        assertEquals(reviewId, groupReview.getReviewId());
        assertEquals(discussionTopic, groupReview.getDiscussionTopic());
        assertEquals(averageRating, groupReview.getAverageRating());
        assertEquals(averageRating, groupReview.getRating());
        assertEquals(reviewComments, groupReview.getReviewComments());
    }

    @Test
    void getStudyGroupReview_reviewNotFound() {
        String reviewId = "review123";

        when(reviewRepository.findByReviewId(reviewId)).thenReturn(Optional.empty());

        StudyGroupReview groupReview = subject.getStudyGroupReview(reviewId);

        assertNull(groupReview);
    }















}






