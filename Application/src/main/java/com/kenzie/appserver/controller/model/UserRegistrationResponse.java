package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegistrationResponse {

    @NotEmpty
    @JsonProperty("userEmail")
    private String userEmail;
    @NotEmpty
    @JsonProperty("registrationStatus")
    private RegistrationStatus registrationStatus;

    @JsonProperty("message")
    private String message;

    @JsonCreator
    public UserRegistrationResponse(@JsonProperty("userEmail")String userEmail, @JsonProperty("registrationStatus")RegistrationStatus registrationStatus,@JsonProperty("message") String message) {
        this.userEmail = userEmail;
        this.registrationStatus = registrationStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
