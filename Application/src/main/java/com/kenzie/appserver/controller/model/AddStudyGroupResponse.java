package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddStudyGroupResponse {
    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("discussionTopic")
    private String discussionTopic;
    @JsonProperty("creationDate")
    private ZonedDateTime creationDate;
    @JsonProperty("active")
    private boolean active;

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
