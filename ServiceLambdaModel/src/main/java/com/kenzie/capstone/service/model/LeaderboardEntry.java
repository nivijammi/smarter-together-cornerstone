package com.kenzie.capstone.service.model;

public class LeaderboardEntry implements Comparable<LeaderboardEntry> {

    private int timeSpentStudying;
    private String userId;

    public LeaderboardEntry(int timeSpentStudying, String userId) {
        this.timeSpentStudying = timeSpentStudying;
        this.userId = userId;
    }

    public LeaderboardEntry(){}

    public int getTimeSpentStudying() {
        return timeSpentStudying;
    }

    public void setTimeSpentStudying(int timeSpentStudying) {
        this.timeSpentStudying = timeSpentStudying;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "timeSpentStudying=" + timeSpentStudying +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public int compareTo(LeaderboardEntry o) {
        return 0;
    }
}
