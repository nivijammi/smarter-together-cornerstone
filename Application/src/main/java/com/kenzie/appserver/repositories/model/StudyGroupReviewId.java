package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Objects;

public class StudyGroupReviewId {
    private String groupId;
    private String reviewId;

    private String discussionTopic;

    /**
     * changes made:
     * 1. added field
     * 2. constructor??
     * 3. added getter and setter for discussionTopic
     * 4. added with annotations
     *
     */
    public StudyGroupReviewId(){
    }

    public StudyGroupReviewId(String groupId, String reviewId) {
        this.groupId = groupId;
        this.reviewId = reviewId;
    }

    @DynamoDBHashKey(attributeName = "GroupId")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    @DynamoDBRangeKey(attributeName = "ReviewId")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "DiscussionTopicIndex")
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyGroupReviewId)) return false;
        StudyGroupReviewId that = (StudyGroupReviewId) o;
        return groupId.equals(that.groupId) && reviewId.equals(that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, reviewId);
    }
}
