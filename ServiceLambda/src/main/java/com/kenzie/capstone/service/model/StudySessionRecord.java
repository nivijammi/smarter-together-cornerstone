package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.ZonedDateTime;

@DynamoDBTable(tableName = "StudySession")
public class StudySessionRecord {


    private String userId;
    private String subject;
    private String sessionId;
    private int duration;
    private ZonedDateTime date; //zonedDateTime here, and string elsewhere because this is a table?
    private String notes;

    //TODO: GSIs?

    @DynamoDBHashKey(attributeName = "UserId")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "UserIdIndex", attributeName = "UserId")
    public String getUserId() {
        return userId;
    }

    @DynamoDBAttribute(attributeName = "Subject")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "SubjectIndex", attributeName = "Subject")
    public String getSubject() {
        return subject;
    }

    @DynamoDBAttribute(attributeName = "SessionId")
    public String getSessionId() {
        return sessionId;
    }

    @DynamoDBAttribute(attributeName = "Duration")

    public int getDuration() {
        return duration;
    }

    @DynamoDBAttribute(attributeName = "Date")
    //placing the next time as a reminder to check why this record uses ZonedDateTime as opposed to string
//    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getDate() {
        return date;
    }

    @DynamoDBAttribute(attributeName = "Notes")
    public String getNotes() {
        return notes;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    //@Override equals with sessionId and userId??
}
