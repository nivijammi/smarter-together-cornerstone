package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.kenzie.appserver.repositories.converter.ZonedDateTimeConverter;
import java.time.ZonedDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "StudyGroup")
public class StudyGroupRecord {
    private String groupId;
    private String groupName;
    private String discussionTopic;
    private ZonedDateTime creationDate;
    private boolean active;

    @DynamoDBHashKey(attributeName = "GroupId")
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    @DynamoDBAttribute(attributeName = "CreationDate")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
    @DynamoDBAttribute(attributeName = "Active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyGroupRecord)) return false;
        StudyGroupRecord that = (StudyGroupRecord) o;
        return groupId.equals(that.groupId) && groupName.equals(that.groupName);
    }
    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName);
    }
    @Override
    public String toString() {
        return "StudyGroupRecord{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", discussionTopic='" + discussionTopic + '\'' +
                ", creationDate=" + creationDate +
                ", active=" + active +
                '}';
    }
}
