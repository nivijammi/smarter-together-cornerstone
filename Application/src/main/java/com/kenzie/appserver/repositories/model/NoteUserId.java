package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.util.Objects;

public class NoteUserId {
    private String noteId;
    private String userId;

    public NoteUserId(){}

    public NoteUserId(String noteId, String userId) {
        this.noteId = noteId;
        this.userId = userId;
    }
    @DynamoDBHashKey(attributeName = "NoteId")
    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
    @DynamoDBRangeKey(attributeName = "UserId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteUserId)) return false;
        NoteUserId noteUserId1 = (NoteUserId) o;
        return noteId.equals(noteUserId1.noteId) && userId.equals(noteUserId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, userId);
    }
}
