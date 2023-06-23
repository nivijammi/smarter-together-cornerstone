package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

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

}
