package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "User")
public class MemberRecord {
    private String email;
    private String password;

    @DynamoDBHashKey(attributeName = "Email")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @DynamoDBAttribute(attributeName = "Password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberRecord)) return false;
        MemberRecord that = (MemberRecord) o;
        return email.equals(that.email) && password.equals(that.password);
    }
    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}


