package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "StudyGroupMember")
public class StudyGroupMemberRecord {

//    private String groupId;
//    private String userId;

   @Id
    private StudyGroupMemberId studyGroupMemberId;
    private String groupName;
    private String discussionTopic;
    private ZonedDateTime creationDate;
    private boolean active;

    public StudyGroupMemberRecord(){}

    public StudyGroupMemberRecord(StudyGroupMemberId studyGroupMemberId, String groupName, String discussionTopic, ZonedDateTime creationDate, boolean active) {
        this.studyGroupMemberId = studyGroupMemberId;
        this.groupName = groupName;
        this.discussionTopic = discussionTopic;
        this.creationDate = creationDate;
        this.active = active;
    }

    @DynamoDBHashKey(attributeName = "GroupId")
    public String getGroupId() {
        return studyGroupMemberId != null ? studyGroupMemberId.getGroupId() : null;
    }
    public void setGroupId(String groupId) {
        if(studyGroupMemberId == null){
            studyGroupMemberId = new StudyGroupMemberId();
        }
        studyGroupMemberId.setGroupId(groupId);
    }

    @DynamoDBRangeKey(attributeName = "UserId")
    //@DynamoDBAttribute(attributeName = "UserId")
    public String getUserId() {
        return studyGroupMemberId != null ? studyGroupMemberId.getUserId() : null;
    }
    public void setUserId(String userId) {
        if(studyGroupMemberId == null){
            studyGroupMemberId = new StudyGroupMemberId();
        }
        studyGroupMemberId.setUserId(userId);
    }
//    @DynamoDBHashKey(attributeName = "GroupId")
//    public String getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(String groupId) {
//        this.groupId = groupId;
//    }
//    @DynamoDBAttribute(attributeName = "UserId")
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
    @DynamoDBAttribute(attributeName = "GroupName")
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @DynamoDBAttribute(attributeName = "discussionTopic")
    public String getDiscussionTopic() {
        return discussionTopic;
    }
    public void setDiscussionTopic(String discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    @DynamoDBAttribute(attributeName = "CreationDate")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
    @DynamoDBAttribute(attributeName = "active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof StudyGroupMemberRecord)) return false;
//        StudyGroupMemberRecord that = (StudyGroupMemberRecord) o;
//        return groupId.equals(that.groupId) && userId.equals(that.userId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(groupId, userId);
//    }
//
//    @Override
//    public String toString() {
//        return "StudyGroupMemberRecord{" +
//                "groupId='" + groupId + '\'' +
//                ", userId='" + userId + '\'' +
//                ", groupName='" + groupName + '\'' +
//                ", discussionTopic='" + discussionTopic + '\'' +
//                ", creationDate=" + creationDate +
//                ", active=" + active +
//                '}';
//    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyGroupMemberRecord)) return false;
        StudyGroupMemberRecord that = (StudyGroupMemberRecord) o;
        return studyGroupMemberId.equals(that.studyGroupMemberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studyGroupMemberId);
    }

    @Override
    public String toString() {
        return "StudyGroupMemberRecord{" +
                "studyGroupMemberId=" + studyGroupMemberId +
                ", groupName='" + groupName + '\'' +
                ", discussionTopic='" + discussionTopic + '\'' +
                ", creationDate=" + creationDate +
                ", active=" + active +
                '}';
    }
}
