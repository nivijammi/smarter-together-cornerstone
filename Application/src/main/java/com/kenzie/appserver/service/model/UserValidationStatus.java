package com.kenzie.appserver.service.model;

public class UserValidationStatus {
    boolean userFound;
    boolean passwordMatched;
    public UserValidationStatus(boolean userFound, boolean passwordMatched) {
        this.userFound = userFound;
        this.passwordMatched = passwordMatched;
    }

    public boolean isUserFound() {
        return userFound;
    }

    public void setUserFound(boolean userFound) {
        this.userFound = userFound;
    }

    public boolean isPasswordMatched() {
        return passwordMatched;
    }

    public void setPasswordMatched(boolean passwordMatched) {
        this.passwordMatched = passwordMatched;
    }
}
