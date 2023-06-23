package com.kenzie.appserver.service.model;

import java.time.ZonedDateTime;

public class StudyGroupMember {
    private String groupId;
    private String memberId;
    private String groupName;
    private String discussionTopic;
    private ZonedDateTime creationDate;
    private boolean active;

    public StudyGroupMember(String groupId, String memberId, String groupName, String discussionTopic, ZonedDateTime creationDate, boolean active) {
        this.groupId = groupId;
        this.memberId = memberId;
        this.groupName = groupName;
        this.discussionTopic = discussionTopic;
        this.creationDate = creationDate;
        this.active = active;
    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDiscussionTopic() {
        return discussionTopic;
    }

    public void setDiscussionTopic(String discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
