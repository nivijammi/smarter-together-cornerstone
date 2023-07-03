package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudyGroupReviewResponse {

    @JsonProperty("groupId")
    private String groupId;

    @JsonProperty("groupName")
    private String groupName;

    @JsonProperty("reviewId")
    private String reviewId;


    @JsonProperty("discussionTopic")
    private String discussionTopic;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("averageRating")
    private double averageRating;

    @JsonProperty("reviewComment")
    private String reviewComment;

    public StudyGroupReviewResponse(){

    }

    @JsonCreator
    public StudyGroupReviewResponse(@JsonProperty("groupId")String groupId,
                                    @JsonProperty("groupName")String groupName,
                                    @JsonProperty("reviewId")String reviewId,
                                    @JsonProperty("discussionTopic")String discussionTopic,
                                    @JsonProperty("rating")int rating,
                                    @JsonProperty("averageRating") double averageRating,
                                    @JsonProperty("reviewComment")String reviewComment) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.reviewId = reviewId;
        this.discussionTopic = discussionTopic;
        this.rating = rating;
        this.averageRating = averageRating;
        this.reviewComment = reviewComment;
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

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
