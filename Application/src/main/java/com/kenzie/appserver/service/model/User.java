package com.kenzie.appserver.service.model;

public class User {

    public String userId;
    public String email;
    public String userName;
    public String password;

    public User(String userId, String email, String userName, String password) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}
