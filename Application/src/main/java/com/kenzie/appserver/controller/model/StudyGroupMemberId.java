package com.kenzie.appserver.controller.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class StudyGroupMemberId {

    @JsonProperty("groupId")
    private String groupId;

    @JsonProperty("userId")
    private String userId;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
