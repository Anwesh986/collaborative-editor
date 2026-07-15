package com.example.demo.dto;

import com.example.demo.entity.ActiveSession;
import java.time.LocalDateTime;
import java.util.List;

public class ActiveUsersResponseMessage {
    private List<ActiveSession> activeUsers;
    private LocalDateTime timestamp;

    public ActiveUsersResponseMessage() {}

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
