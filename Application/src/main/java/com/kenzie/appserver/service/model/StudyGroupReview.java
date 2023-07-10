package com.kenzie.appserver.service.model;

public class StudyGroupReview {

    private String groupId;
    private String groupName;
    private String reviewId;

    private String discussionTopic;
    private double rating;

    private double averageRating;
    private String reviewComments;

    public StudyGroupReview(){}

    public StudyGroupReview(String groupId, String groupName, String reviewId, String discussionTopic, double rating, double averageRating, String reviewComments) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.reviewId = reviewId;
        this.discussionTopic = discussionTopic;
        this.rating = rating;
        this.averageRating = averageRating;
        this.reviewComments = reviewComments;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(String discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }
}
