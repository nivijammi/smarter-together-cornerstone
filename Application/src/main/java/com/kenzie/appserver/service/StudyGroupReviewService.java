package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.kenzie.appserver.controller.model.GroupReviewResponse;
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




//
//    public List<String> getGroupsWithDesiredRating1(String discussionTopic, double rating) {
//
//        final double HIGHEST_RATING = 5.0;
//        Map<String, List<Double>> groupRatings = new HashMap<>();
//
//
//        Iterable<StudyGroupReviewRecord> allRecords = reviewRepository.findAll();
//
//        if(allRecords !=null) {
//
//            for (StudyGroupReviewRecord reviewRecord : allRecords) {
//                if (Objects.equals(reviewRecord.getDiscussionTopic(), discussionTopic)) {
//                    String groupId = reviewRecord.getGroupId();
//                    double averageRating = calculateAverageRating(groupId);
//
//                    if (averageRating >= rating) {
//                        groupRatings.putIfAbsent(groupId, new ArrayList<>());
//                        groupRatings.get(groupId).add(averageRating);
//                    }
//                }
//            }
//        }
//        Map<String, Double> condensedResult = new HashMap<>();
//        for (Map.Entry<String, List<Double>> entry : groupRatings.entrySet()) {
//            String groupId = entry.getKey();
//            List<Double> ratings = entry.getValue();
//            double averageRating = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
//
//            condensedResult.put(groupId, averageRating);
//        }
//        return groupsWithDesiredRating;
//    }

    public Map<String, Double> getGroupsWithDesiredRating(double rating,String discussionTopic) {
        // Map to store ids and ratings
        Map<String, List<Double>> groupRatings = new HashMap<>();

        // all records
        Iterable<StudyGroupReviewRecord> allRecords = reviewRepository.findAll();


        for (StudyGroupReviewRecord reviewRecord : allRecords) {
            // Check if the discussion topic matches the desired topic
            if (Objects.equals(reviewRecord.getDiscussionTopic(), discussionTopic)) {
                String groupId = reviewRecord.getGroupId();
                double currentRating = reviewRecord.getRating();

                // Check if the average rating greater or equal to desired rating
                if (currentRating >= rating) {
                    // If the group ID is already present in the map, add the rating to its list
                    // Otherwise, create a new list and add the rating to it
                    if (groupRatings.containsKey(groupId)) {
                        groupRatings.get(groupId).add(currentRating);
                    } else {
                        List<Double> ratings = new ArrayList<>();
                        ratings.add(currentRating);
                        groupRatings.put(groupId, ratings);
                    }
                }
            }
        }

        //  Condensed result with group IDs and their average ratings
        Map<String, Double> condensedResult = new HashMap<>();

        // Calculate the average rating for each group and add it to the condensed result map
        for (Map.Entry<String, List<Double>> entry : groupRatings.entrySet()) {
            String groupId = entry.getKey();
            List<Double> ratings = entry.getValue();

            // Calculate the average rating by taking the sum and dividing it by the number of ratings
            //double averageRating = calculateAverageRating(groupId);
            double sum = 0.0;
            for (double ratingValue : ratings) {
                sum += ratingValue;
            }
            double average = sum / ratings.size();

            condensedResult.put(groupId, average);
        }

        // Return the condensed result map
        return condensedResult;
    }

    public double calculateAverageRating(String id) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(id);
        List<StudyGroupReviewRecord> reviews = byGroupId.orElse(Collections.emptyList());
        double totalRating = 0;
        double reviewCount = reviews.size();

        for (StudyGroupReviewRecord reviewRecord  : reviews) {
            totalRating += reviewRecord.getRating();
        }
        double averageRating = (reviewCount > 0) ? (double) totalRating / reviewCount : 0.0;

        return  averageRating;

    }


    public void deleteGroupFromReviewRecord(String groupId) {
        Optional<List<StudyGroupReviewRecord>> byGroupId = reviewRepository.findByGroupId(groupId);

        if (byGroupId.isPresent()) {
            List<StudyGroupReviewRecord> studyGroupReviewRecords = byGroupId.get();
            reviewRepository.deleteAll(studyGroupReviewRecords);
        } else {
            throw new ReviewNotFoundException("No study group reviews found for groupId: " + groupId);
        }
    }

}
