package com.kenzie.appserver.service.model;


public class StudyGroupMemberId {
    private String groupId;
    private String userId;

    public StudyGroupMemberId() {
    }

    public StudyGroupMemberId(String groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }


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
