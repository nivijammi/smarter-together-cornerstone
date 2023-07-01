package com.kenzie.capstone.service.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.kenzie.capstone.service.converter.ZonedDateTimeConverter;

import java.time.ZonedDateTime;

@DynamoDBTable(tableName = "StudySessionTable")
public class StudySessionRecord {


    private String userId;
    private String subject;
    private String sessionId;
    private int duration;
    private ZonedDateTime date; //zonedDateTime here, and string elsewhere because this is a table?
    private String notes;

    //TODO: GSIs?
    @DynamoDBHashKey(attributeName = "sessionId")
    public String getSessionId() {
        return sessionId;
    }
    @DynamoDBAttribute(attributeName = "userId")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "userIdIndex", attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    @DynamoDBAttribute(attributeName = "subject")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "SubjectIndex", attributeName = "Subject")
    public String getSubject() {
        return subject;
    }



    @DynamoDBAttribute(attributeName = "duration")
    public int getDuration() {
        return duration;
    }

    @DynamoDBAttribute(attributeName = "date")
    //placing the next time as a reminder to check why this record uses ZonedDateTime as opposed to string
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getDate() {
        return date;
    }

    @DynamoDBAttribute(attributeName = "notes")
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
