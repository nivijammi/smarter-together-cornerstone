package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@DynamoDBTable(tableName = "StudyGroupReview")
public class StudyGroupReviewRecord {

    @Id
    private StudyGroupReviewId studyGroupReviewId;
    private String groupName;
    private String discussionTopic;
    private double rating;
    private String reviewComments;
    private double averageRating;

    public StudyGroupReviewRecord(){}

    public StudyGroupReviewRecord(StudyGroupReviewId studyGroupReviewId, String groupName, String discussionTopic, double rating, String reviewComments, double averageRating) {
        this.studyGroupReviewId = studyGroupReviewId;
        this.groupName = groupName;
        this.discussionTopic = discussionTopic;
        this.rating = rating;
        this.reviewComments = reviewComments;
        this.averageRating = averageRating;
    }
    @DynamoDBHashKey(attributeName = "GroupId")
    public String getGroupId() {
        return studyGroupReviewId != null ? studyGroupReviewId.getGroupId() : null;
    }
    public void setGroupId(String groupId) {
        if(studyGroupReviewId == null){
            studyGroupReviewId = new StudyGroupReviewId();
        }
        studyGroupReviewId.setGroupId(groupId);
    }

    @DynamoDBRangeKey(attributeName = "ReviewId")
    public String getReviewId() {
        return studyGroupReviewId != null ? studyGroupReviewId.getReviewId() : null;
    }
    public void setReviewId(String reviewId) {
        if(studyGroupReviewId == null){
            studyGroupReviewId = new StudyGroupReviewId();
        }
        studyGroupReviewId.setReviewId(reviewId);
    }


    @DynamoDBAttribute(attributeName = "GroupName")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    @DynamoDBAttribute(attributeName = "DiscussionTopic")
    public String getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(String discussionTopic) {
        this.discussionTopic = discussionTopic;
    }
    @DynamoDBAttribute(attributeName = "Rating")
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    @DynamoDBAttribute(attributeName = "ReviewComments")
    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }
    @DynamoDBAttribute(attributeName = "AverageRating")
    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyGroupReviewRecord)) return false;
        StudyGroupReviewRecord that = (StudyGroupReviewRecord) o;
        return studyGroupReviewId.equals(that.studyGroupReviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studyGroupReviewId);
    }

    @Override
    public String toString() {
        return "StudyGroupReviewRecord{" +
                "studyGroupReviewId=" + studyGroupReviewId +
                ", groupName='" + groupName + '\'' +
                ", discussionTopic='" + discussionTopic + '\'' +
                ", rating=" + rating +
                ", reviewComments='" + reviewComments + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }
}
