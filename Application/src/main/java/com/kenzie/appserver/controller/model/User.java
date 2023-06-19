package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("email")
    public String email;

    @JsonProperty("userName")
    public String userName;

    @JsonProperty("password")
    public String password;

    @JsonProperty("userId")
    public String userId;


}
