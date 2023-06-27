package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.time.ZonedDateTime;
import java.util.Objects;

@DynamoDBTable(tableName = "Note")
public class NoteRecord {
    private String noteId;
    private String userId;
    private String content;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updatedDateTime;

    @DynamoDBHashKey(attributeName = "NoteId")
    public String getNoteId() {
        return noteId;
    }
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    @DynamoDBRangeKey(attributeName = "UserId")
    public String getUserId() { return userId; }
    public void setUserId(String userId) {this.userId = userId;}
    @DynamoDBAttribute(attributeName = "Content")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @DynamoDBAttribute(attributeName = "CreatedDateTime")
    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }
    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
    @DynamoDBAttribute(attributeName = "UpdatedDateTime")
    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }
    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteRecord)) return false;
        NoteRecord that = (NoteRecord) o;
        return noteId.equals(that.noteId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(noteId);
    }
}
