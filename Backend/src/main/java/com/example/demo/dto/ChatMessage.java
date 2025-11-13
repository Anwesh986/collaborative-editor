package com.example.demo.dto;

public class ChatMessage {
    private String roomId;
    private String userId;
    private String username;
    private String message;
    private long timestamp;
    private String messageType; // "text", "system", "code-snippet"
    
    // Constructors
    public ChatMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ChatMessage(String roomId, String userId, String username, String message, String messageType) {
        this();
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.message = message;
        this.messageType = messageType;
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
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
