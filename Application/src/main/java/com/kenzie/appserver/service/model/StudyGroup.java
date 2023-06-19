package com.kenzie.appserver.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StudyGroup {

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

    public StudyGroup(String groupId, String groupName, List<String> members,
                      String subject, String topic, String creationDate,
                      String endDate, String meetingTime, int duration, boolean successful) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.members = members;
        this.subject = subject;
        this.topic = topic;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.meetingTime = meetingTime;
        this.duration = duration;
        this.successful = successful;
    }

    public StudyGroup(String groupName,
                      String subject, String topic, String creationDate,
                      String endDate, String meetingTime, int duration){
        this.groupName = groupName;
        this.subject = subject;
        this.topic = topic;
        this.creationDate = creationDate;
        this.endDate = endDate;
        this.meetingTime = meetingTime;
        this.duration = duration;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getSubject() {
        return subject;
    }

    public String getTopic() {
        return topic;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
