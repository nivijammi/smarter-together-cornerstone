package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class UserLoginRequest {


    @NotEmpty
    @JsonProperty("email")
    private String email;


    @NotEmpty
    @JsonProperty("password")
    private String password;

    // getter and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
