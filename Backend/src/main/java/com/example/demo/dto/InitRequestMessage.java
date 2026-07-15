package com.example.demo.dto;

import java.time.LocalDateTime;

public class InitRequestMessage {
    private String username;

    public InitRequestMessage() {}

    public InitRequestMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
