package com.example.demo.dto;

import java.time.LocalDateTime;

public class HeartbeatMessage {
    private String userId;
    private LocalDateTime timestamp;

    public HeartbeatMessage() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
