package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse {

    @NotEmpty
    @JsonProperty("token")
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
