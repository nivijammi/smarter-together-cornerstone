package com.kenzie.capstone.service.model;

import java.util.List;

public class StudySession {

    private String sessionId;
    private String userId;
    private String subject;
    private int duration;
    private String date;
    private String notes;  // does it need to be a list? Now I think its just a quick note, no need for list

    public StudySession(String sessionId, String userId, String subject, int duration, String date, String notes) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.subject = subject;
        this.duration = duration;
        this.date = date;
        this.notes = notes;
    }

    public StudySession() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    //need to implement Override equals and hashCode, figure out what params are needed for it


    @Override
    public String toString() {
        return "StudySession{" +
                "sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", subject='" + subject + '\'' +
                ", duration=" + duration +
                ", date='" + date + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
