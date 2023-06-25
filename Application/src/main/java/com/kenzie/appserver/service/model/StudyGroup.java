package com.kenzie.appserver.service.model;

import java.time.ZonedDateTime;
import java.util.List;

public class StudyGroup {

    private String groupId;
    private String groupName;
    private String discussionTopic;
    private ZonedDateTime creationDate;
    private boolean active;
    public StudyGroup(String groupId, String groupName,
                      String discussionTopic, ZonedDateTime creationDate,
                      boolean active) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.discussionTopic = discussionTopic;
        this.creationDate = creationDate;
        this.active = active;
    }

    public StudyGroup() {

    }

    public String getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }
    public String getDiscussionTopic() {
        return discussionTopic;
    }
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
    public boolean isActive() {
        return active;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDiscussionTopic(String discussionTopic) {
        this.discussionTopic = discussionTopic;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
