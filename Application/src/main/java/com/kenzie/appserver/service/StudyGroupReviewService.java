package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.StudyGroupReviewResponse;
import com.kenzie.appserver.exception.ReviewNotFoundException;
import com.kenzie.appserver.repositories.StudyGroupReviewRepository;
import com.kenzie.appserver.repositories.model.StudyGroupReviewId;
import com.kenzie.appserver.repositories.model.StudyGroupReviewRecord;
import com.kenzie.appserver.service.model.StudyGroupReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudyGroupReviewService {
   @Autowired
    private StudyGroupReviewRepository reviewRepository;

    public StudyGroupReview submitStudyGroupReview(StudyGroupReview review) {
        // Validate the review data and perform any necessary checks
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
        // Save the review to the repository
        StudyGroupReviewRecord record = getStudyGroupReviewRecord(review);
        reviewRepository.save(record);

        // Calculate and update the average rating for the associated study group
        StudyGroupReviewId studyGroupReviewId = new StudyGroupReviewId(review.getGroupId(),review.getReviewId());
        double averageRating = calculateAverageRating(studyGroupReviewId);

        // Collect all comments for the study group
        List<String> comments = collectCommentsForStudyGroup(review.getGroupId());
        double totalRating = collectRatingsForStudyGroup(review.getGroupId());

        StudyGroupReview groupReview = new StudyGroupReview();
        groupReview.setGroupId(studyGroupReviewId.getGroupId());
        groupReview.setReviewId(studyGroupReviewId.getReviewId());
        groupReview.setGroupName(record.getGroupName());
        groupReview.setDiscussionTopic(record.getDiscussionTopic());
        groupReview.setRating(totalRating);
        groupReview.setAverageRating(averageRating);
        groupReview.setReviewComments(comments.toString());

        return groupReview;


    }
    private double collectRatingsForStudyGroup(String groupId) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(groupId);
        List<StudyGroupReviewRecord> ratingList = byGroupId.get();

        double totalRating = 0.0;
        for (StudyGroupReviewRecord rating : ratingList) {
            totalRating = totalRating + rating.getRating();
        }
        return totalRating;
    }



    private List<String> collectCommentsForStudyGroup(String groupId) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(groupId);
        List<StudyGroupReviewRecord> recordList = byGroupId.get();
        List<String> comments = new ArrayList<>();

        for (StudyGroupReviewRecord record : recordList) {
            comments.add(record.getReviewComments());
        }

        return comments;
    }

    public static StudyGroupReviewRecord getStudyGroupReviewRecord(StudyGroupReview review) {

        StudyGroupReviewRecord record = new StudyGroupReviewRecord();
        record.setGroupId(review.getGroupId());
        record.setReviewId(UUID.randomUUID().toString());
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

    public double calculateAverageRating(StudyGroupReviewId id) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(id.getGroupId());
        List<StudyGroupReviewRecord> reviews = byGroupId.get();
        int totalRating = 0;
        int reviewCount = reviews.size();

        for (StudyGroupReviewRecord reviewRecord  : reviews) {
            totalRating += reviewRecord.getAverageRating();
        }
         return (reviewCount > 0) ? (double) totalRating / reviewCount : 0.0;

    }

}
