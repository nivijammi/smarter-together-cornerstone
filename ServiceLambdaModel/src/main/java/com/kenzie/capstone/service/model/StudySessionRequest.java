package com.kenzie.capstone.service.model;

public class StudySessionRequest {


    private String userId;
    private String subject;
    private int duration;
    private String date;
    private String notes;  // does it need to be a list? Now I think its just a quick note, no need for list

    //make a new constructor here, without a username... that will then dip into the userRepository to grab it?
    public StudySessionRequest(String userId, String subject, int duration, String date, String notes) {
        this.userId = userId;
        this.subject = subject;
        this.duration = duration;
        this.date = date;
        this.notes = notes;
    }

    public StudySessionRequest() {}

    public String getUserId() {
        return userId;
    }

    public String getSubject() {
        return subject;
    }

    public int getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "StudySessionRequest{" +
                "userId='" + userId + '\'' +
                ", subject='" + subject + '\'' +
                ", duration=" + duration +
                ", date='" + date + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
