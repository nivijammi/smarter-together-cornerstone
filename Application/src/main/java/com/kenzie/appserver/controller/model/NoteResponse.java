package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;

public class NoteResponse {
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
    private String createdDateTime;

    @NotEmpty
    @JsonProperty("updatedDateTime")
    private String updatedDateTime;

    @JsonCreator
    public NoteResponse(@JsonProperty("noteId") String noteId, @JsonProperty("userId")String userId, @JsonProperty("content")String content, @JsonProperty("createdDateTime")String createdDateTime, @JsonProperty("updatedDateTime")String updatedDateTime) {
        this.noteId = noteId;
        this.userId = userId;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

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

    public String getCreatedDateTime() {
        return createdDateTime;
    }
    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getUpdatedDateTime() {return updatedDateTime;}
    public void setUpdatedDateTime(String updatedDateTime) {this.updatedDateTime = updatedDateTime;}
}
