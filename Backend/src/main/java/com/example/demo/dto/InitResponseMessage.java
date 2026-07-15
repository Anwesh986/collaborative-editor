package com.example.demo.dto;

import com.example.demo.entity.ActiveSession;
import java.time.LocalDateTime;
import java.util.List;

public class InitResponseMessage {
    private String userId;
    private String username;
    private String userColor;
    private String code;
    private String language;
    private List<ActiveSession> activeUsers;
    private LocalDateTime timestamp;

    public InitResponseMessage() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserColor() {
        return userColor;
    }

    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<ActiveSession> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(List<ActiveSession> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
