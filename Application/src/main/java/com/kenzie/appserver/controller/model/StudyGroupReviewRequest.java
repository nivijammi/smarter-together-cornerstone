package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class StudyGroupReviewRequest {
    @NotEmpty
    @JsonProperty("groupId")
    private String groupId;
    @NotEmpty
    @JsonProperty("groupName")
    private String groupName;
    @NotEmpty
    @JsonProperty("discussionTopic")
    private String discussionTopic;
    @NotEmpty
    @JsonProperty("rating")
    private double rating;
    @NotEmpty
    @JsonProperty("reviewComment")
    private String reviewComment;


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

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComments) {
        this.reviewComment = reviewComments;
    }
}
