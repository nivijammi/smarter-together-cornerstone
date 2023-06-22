package com.kenzie.appserver.service.model;

public class UserDetail {

    private String userEmail;
    private String hashedPassword;

    public UserDetail( String userEmail, String hashedPassword) {
        this.userEmail = userEmail;
        this.hashedPassword = hashedPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
