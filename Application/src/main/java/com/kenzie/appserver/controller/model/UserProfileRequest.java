package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;

public class UserProfileRequest {
    @NotEmpty
    @JsonProperty("firstName")
    private String firstName;

    @NotEmpty
    @JsonProperty("lastName")
    private String lastName;

    @NotEmpty
    @JsonProperty("email")
    private String email;

    @NotEmpty
    @JsonProperty("password")
    private String password;

    @NotEmpty
    @JsonProperty("creationDate")
    private ZonedDateTime creationDate;

    // getter and setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String email) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String email) {
        this.lastName = lastName;
    }
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

    public ZonedDateTime getCreationDate() {return creationDate;}

    public void setCreationDate(ZonedDateTime creationDate) {this.creationDate = creationDate;}
}
