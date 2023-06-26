package com.kenzie.appserver.service.model;

import java.time.ZonedDateTime;

public class User {
    private String email;

    private String password;

    private ZonedDateTime creationDate;

    public User(String email, String password, ZonedDateTime creationDate) {
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
