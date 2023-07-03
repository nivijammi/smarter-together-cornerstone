package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import org.springframework.data.annotation.Id;

import java.time.ZonedDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "StudyGroupMember")
public class StudyGroupMemberRecord {
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

    @DynamoDBRangeKey(attributeName = "MemberId")
    public String getMemberId() {
        return studyGroupMemberId != null ? studyGroupMemberId.getMemberId() : null;
    }
    public void setMemberId(String memberId) {
        if(studyGroupMemberId == null){
            studyGroupMemberId = new StudyGroupMemberId();
        }
        studyGroupMemberId.setMemberId(memberId);
    }
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
