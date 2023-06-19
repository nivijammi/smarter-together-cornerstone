package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "StudyGroup")
public class StudyGroupRecord {
    private String groupId;

    private String groupName;

    private List<String> members;

    private String subject;

    private String topic;

    private String creationDate;

    private String endDate;

    private String meetingTime;

    private int duration;

    private boolean successful;

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
    @DynamoDBAttribute(attributeName = "GroupMembers")
    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
    @DynamoDBAttribute(attributeName = "Subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    //@DynamoDBIndexHashKey(globalSecondaryIndexName = "Topic", attributeName = "Topic")
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
    @DynamoDBAttribute(attributeName = "CreationDate")

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    @DynamoDBAttribute(attributeName = "EndDate")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    @DynamoDBAttribute(attributeName = "Time")
    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }
    @DynamoDBAttribute(attributeName = "Duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    @DynamoDBAttribute(attributeName = "IsSuccessful")
    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyGroupRecord)) return false;
        StudyGroupRecord that = (StudyGroupRecord) o;
        return duration == that.duration && successful == that.successful && Objects.equals(groupId, that.groupId) && Objects.equals(groupName, that.groupName) && Objects.equals(members, that.members) && Objects.equals(subject, that.subject) && Objects.equals(topic, that.topic) && Objects.equals(creationDate, that.creationDate) && Objects.equals(endDate, that.endDate) && Objects.equals(meetingTime, that.meetingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, members, subject, topic, creationDate, endDate, meetingTime, duration, successful);
    }

    @Override
    public String toString() {
        return "StudyGroupRecord{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", members=" + members +
                ", subject='" + subject + '\'' +
                ", topic='" + topic + '\'' +
                ", creationDate=" + creationDate +
                ", endDate=" + endDate +
                ", meetingTime=" + meetingTime +
                ", duration=" + duration +
                ", successful=" + successful +
                '}';
    }
}
