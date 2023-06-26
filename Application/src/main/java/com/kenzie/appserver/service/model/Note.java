package com.kenzie.appserver.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;

public class Note {
    private String noteId;
    private String userId;
    private String content;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;

    public Note(String noteId, String userId, String content, ZonedDateTime createdDateTime, ZonedDateTime updatedDateTime) {
        this.noteId = noteId;
        this.userId = userId;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
}
