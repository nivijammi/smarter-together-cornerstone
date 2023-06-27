package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;

public class NoteRequest {
    @NotEmpty
    @JsonProperty("noteId")
    private String noteId;

    @NotEmpty
    @JsonProperty("userId")
    private String userId;

    @NotEmpty
    @JsonProperty("content")
    private String content;

    @NotEmpty
    @JsonProperty("createdDateTime")
    private ZonedDateTime createdDateTime;

    @NotEmpty
    @JsonProperty("updatedDateTime")
    private ZonedDateTime updatedDateTime;

    // getter and setters

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
    public void setCreatedDateTime(ZonedDateTime password) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getUpdatedDateTime() {return updatedDateTime;}
    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {this.updatedDateTime = updatedDateTime;}
}
