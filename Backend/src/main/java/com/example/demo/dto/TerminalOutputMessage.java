package com.example.demo.dto;

import java.util.List;

public class TerminalOutputMessage {
    private String roomId;
    private String userId;
    private String username;
    private List<Object> outputs; // Array of terminal output objects
    private long timestamp;
    private String language;
    
    // Constructors
    public TerminalOutputMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public TerminalOutputMessage(String roomId, String userId, String username, List<Object> outputs, String language) {
        this();
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.outputs = outputs;
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
    
    public List<Object> getOutputs() {
        return outputs;
    }
    
    public void setOutputs(List<Object> outputs) {
        this.outputs = outputs;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
}
