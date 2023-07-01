package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.kenzie.appserver.controller.model.StudyGroupReviewResponse;
import com.kenzie.appserver.exception.ReviewNotFoundException;
import com.kenzie.appserver.repositories.StudyGroupReviewRepository;
import com.kenzie.appserver.repositories.model.StudyGroupReviewId;
import com.kenzie.appserver.repositories.model.StudyGroupReviewRecord;
import com.kenzie.appserver.service.model.StudyGroupReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudyGroupReviewService {
   @Autowired
    private StudyGroupReviewRepository reviewRepository;

    public StudyGroupReviewService(StudyGroupReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public StudyGroupReview submitStudyGroupReview(StudyGroupReview review) {
        if (review == null) {
            throw new ReviewNotFoundException("Review cannot be null");
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new ReviewNotFoundException("Rating must be between 1 and 5");
        }
        if (review.getReviewComments() == null || review.getReviewComments().isEmpty()) {
            throw new ReviewNotFoundException("Comments cannot be empty");
        }

        StudyGroupReviewId reviewId = new StudyGroupReviewId(review.getGroupId(), review.getReviewId());

        StudyGroupReviewRecord record = getStudyGroupReviewRecord(review);
        reviewRepository.save(record);

        double averageRating = calculateAverageRating(review.getGroupId());

        StudyGroupReview groupReview = new StudyGroupReview();
        groupReview.setGroupId(record.getGroupId());
        groupReview.setReviewId(record.getReviewId());
        groupReview.setGroupName(record.getGroupName());
        groupReview.setDiscussionTopic(record.getDiscussionTopic());
        groupReview.setRating(record.getRating());
        groupReview.setAverageRating(averageRating);
        groupReview.setReviewComments(record.getReviewComments());

        return groupReview;


    }
    public double collectRatingsForStudyGroup(String groupId) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(groupId);
        List<StudyGroupReviewRecord> ratingList = byGroupId.orElse(Collections.emptyList());

        double totalRating = 0.0;
        for (StudyGroupReviewRecord rating : ratingList) {
            totalRating = totalRating + rating.getRating();
        }
        return totalRating;
    }



    public List<String> collectCommentsForStudyGroup(String groupId) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(groupId);
        List<StudyGroupReviewRecord> recordList = byGroupId.orElse(Collections.emptyList());
        List<String> comments = new ArrayList<>();

        for (StudyGroupReviewRecord record : recordList) {
            comments.add(record.getReviewComments());
        }

        return comments;
    }
    // helper should not ever make a UUID,
    // it should be as versatile as possible, to be used by other methods
    public StudyGroupReviewRecord getStudyGroupReviewRecord(StudyGroupReview review) {

        StudyGroupReviewRecord record = new StudyGroupReviewRecord();
        record.setGroupId(review.getGroupId());
        record.setReviewId(review.getReviewId());
        record.setGroupName(review.getGroupName());
        record.setRating(review.getRating());
        record.setAverageRating(review.getRating());
        record.setDiscussionTopic(review.getDiscussionTopic());
        record.setReviewComments(review.getReviewComments());
        return record;
    }


    public StudyGroupReview getStudyGroupReview(String reviewId) {
        Optional<StudyGroupReviewRecord> optionalStudyGroupReview = reviewRepository.findByReviewId(reviewId);
        if (optionalStudyGroupReview.isPresent()) {
            StudyGroupReviewRecord record = optionalStudyGroupReview.get();

            StudyGroupReviewId id = new StudyGroupReviewId(record.getGroupId(),reviewId);
            StudyGroupReview review = new StudyGroupReview(id.getGroupId(), record.getGroupName(), reviewId, record.getDiscussionTopic(), record.getAverageRating(), record.getAverageRating(), record.getReviewComments());
            return review;
        }

        return null;
    }

    public List<StudyGroupReview> getStudyGroupReviewsByTopic(String discussionTopic) {

        Optional<List<StudyGroupReviewRecord>> byTopic = reviewRepository.findByDiscussionTopic(discussionTopic);
        List<StudyGroupReviewRecord> reviewRecords = byTopic.orElse(Collections.emptyList());

        List<StudyGroupReview> reviews = new ArrayList<>();
        for (StudyGroupReviewRecord record:reviewRecords){
            reviews.add(buildStudyGroupReview(record));
        }
        return reviews;
    }

    public StudyGroupReview buildStudyGroupReview(StudyGroupReviewRecord record){
        StudyGroupReview groupReview = new StudyGroupReview();
        groupReview.setGroupId(record.getGroupId());
        groupReview.setReviewId(record.getReviewId());
        groupReview.setGroupName(record.getGroupName());
        groupReview.setDiscussionTopic(record.getDiscussionTopic());
        groupReview.setRating(record.getRating());
        groupReview.setAverageRating(record.getAverageRating());
        groupReview.setReviewComments(record.getReviewComments());

        return groupReview;
    }



    public double calculateAverageRating(String id) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(id);
        //List<StudyGroupReviewRecord> reviews = byGroupId.get();
        List<StudyGroupReviewRecord> reviews = byGroupId.orElse(Collections.emptyList());
        double totalRating = 0;
        double reviewCount = reviews.size();

        for (StudyGroupReviewRecord reviewRecord  : reviews) {
            totalRating += reviewRecord.getRating();
        }
         return (reviewCount > 0) ? (double) totalRating / reviewCount : 0.0;

    }

}
