package com.example.demo.dto;

public class CodeChangeMessage {
    private String roomId;
    private String userId;
    private String username;
    private String changeType; // "insert", "delete", "replace"
    private int startPosition;
    private int endPosition;
    private String content;
    private long timestamp;
    private int version; // For conflict resolution
    
    // Constructors
    public CodeChangeMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public CodeChangeMessage(String roomId, String userId, String username, String changeType, 
                            int startPosition, int endPosition, String content, int version) {
        this();
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.changeType = changeType;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.content = content;
        this.version = version;
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
    
    public String getChangeType() {
        return changeType;
    }
    
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
    
    public int getStartPosition() {
        return startPosition;
    }
    
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }
    
    public int getEndPosition() {
        return endPosition;
    }
    
    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
}
