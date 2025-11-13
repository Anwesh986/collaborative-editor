package com.example.demo.dto;

public class UserJoinedMessage {
    private String roomId;
    private String userId;
    private String username;
    private String action; // "joined" or "left"
    private long timestamp;
    private int activeUserCount;
    
    // Constructors
    public UserJoinedMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public UserJoinedMessage(String roomId, String userId, String username, String action, int activeUserCount) {
        this();
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.activeUserCount = activeUserCount;
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
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getActiveUserCount() {
        return activeUserCount;
    }
    
    public void setActiveUserCount(int activeUserCount) {
        this.activeUserCount = activeUserCount;
    }
}
