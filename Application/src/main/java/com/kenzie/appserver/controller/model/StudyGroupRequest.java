package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

import java.time.LocalTime;


public class StudyGroupRequest {

    @NotEmpty
    @JsonProperty("groupName")
    private String groupName;
    @NotEmpty
    @JsonProperty("subject")
    private String subject;
    @NotEmpty
    @JsonProperty("topic")
    private String topic;
    @NotEmpty
    @JsonProperty("creationDate")
    private String creationDate;
    @NotEmpty
    @JsonProperty("endDate")
    private String endDate;
    @NotEmpty
    @JsonProperty("meetingTime")
    private String meetingTime;

    @Min(0)
    @JsonProperty("duration")
    private int duration;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
