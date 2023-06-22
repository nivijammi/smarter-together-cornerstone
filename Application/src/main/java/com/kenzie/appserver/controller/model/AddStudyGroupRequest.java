package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

import java.time.ZonedDateTime;


public class AddStudyGroupRequest {

    @NotEmpty
    @JsonProperty("groupName")
    private String groupName;
    @NotEmpty
    @JsonProperty("discussionTopic")
    private String discussionTopic;
    @JsonProperty("creationDate")
    private ZonedDateTime creationDate;
    @JsonProperty("active")
    private boolean active;

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
