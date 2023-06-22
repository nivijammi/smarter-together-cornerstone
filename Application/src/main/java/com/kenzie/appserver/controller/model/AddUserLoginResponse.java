package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddUserLoginResponse {

    @NotEmpty
    @JsonProperty("userEmail")
    private String userEmail;
    @NotEmpty
    @JsonProperty("registrationStatus")
    private RegistrationStatus registrationStatus;

    public AddUserLoginResponse(String userEmail, RegistrationStatus registrationStatus) {
        this.userEmail = userEmail;
        this.registrationStatus = registrationStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
}
