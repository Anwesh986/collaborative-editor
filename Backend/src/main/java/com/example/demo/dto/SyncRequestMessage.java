package com.example.demo.dto;

public class SyncRequestMessage {
    private String roomId;
    private String userId;
    private String username;
    private int lastKnownVersion; // Version number of last change client has
    
    // Constructors
    public SyncRequestMessage() {}
    
    public SyncRequestMessage(String roomId, String userId, String username, int lastKnownVersion) {
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.lastKnownVersion = lastKnownVersion;
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
    
    public int getLastKnownVersion() {
        return lastKnownVersion;
    }
    
    public void setLastKnownVersion(int lastKnownVersion) {
        this.lastKnownVersion = lastKnownVersion;
    }
}
