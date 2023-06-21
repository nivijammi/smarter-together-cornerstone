package com.kenzie.appserver.arc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserNotBeingUsedAnymore {
    @JsonProperty("userId")
    public String userId;
    @JsonProperty("userName")
    public String userName;
    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
