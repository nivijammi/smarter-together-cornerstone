package com.kenzie.appserver.service.model;

import java.time.ZonedDateTime;

public class User {
    private String email;

    private String password;

    private String lastName;

    private String firstName;

    private ZonedDateTime creationDate;

    public User(String email, String password, String lastName, String firstName, ZonedDateTime creationDate) {
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
