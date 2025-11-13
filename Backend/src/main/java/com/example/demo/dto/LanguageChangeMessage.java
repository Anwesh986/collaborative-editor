package com.example.demo.dto;

public class LanguageChangeMessage {
    private String roomId;
    private String userId;
    private String username;
    private String language;
    private long timestamp;
    
    // Constructors
    public LanguageChangeMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public LanguageChangeMessage(String roomId, String userId, String username, String language) {
        this();
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.language = language;
    }
    
    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
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
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
