package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupReviewResponse {

        @JsonProperty("groupId")
        private String groupId;

        @JsonProperty("groupName")
        private String groupName;

        @JsonProperty("reviewId")
        private String reviewId;

        @JsonProperty("discussionTopic")
        private String discussionTopic;

        @JsonProperty("totalRating")
        private double totalRating;

        @JsonProperty("averageRating")
        private double averageRating;

        @JsonProperty("reviewComments")
        private String reviewComments;

    public GroupReviewResponse(){}

    @JsonCreator
    public GroupReviewResponse(@JsonProperty("groupId")String groupId,
                                    @JsonProperty("groupName")String groupName,
                                    @JsonProperty("reviewId")String reviewId,
                                    @JsonProperty("discussionTopic")String discussionTopic,
                                    @JsonProperty("totalRating")double totalRating,
                                    @JsonProperty("averageRating") double averageRating,
                                    @JsonProperty("reviewComments")String reviewComments) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.reviewId = reviewId;
        this.discussionTopic = discussionTopic;
        this.totalRating = totalRating;
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

    public double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
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
