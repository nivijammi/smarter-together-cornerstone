package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.util.Objects;

public class StudyGroupMemberId {
    private String groupId;
    private String memberId;

    public StudyGroupMemberId() {
    }

    public StudyGroupMemberId(String groupId, String memberId) {
        this.groupId = groupId;
        this.memberId = memberId;
    }

    @DynamoDBHashKey(attributeName = "GroupId")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @DynamoDBRangeKey(attributeName = "MemberId")
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyGroupMemberId)) return false;
        StudyGroupMemberId that = (StudyGroupMemberId) o;
        return groupId.equals(that.groupId) && memberId.equals(that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, memberId);
    }
}
