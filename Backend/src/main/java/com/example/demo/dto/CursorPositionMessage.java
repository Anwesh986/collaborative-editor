package com.example.demo.dto;

public class CursorPositionMessage {
    private String roomId;
    private String userId;
    private String username;
    private int position;
    private int lineNumber;
    private int column;
    private String color; // For displaying user cursor in different colors
    
    // Constructors
    public CursorPositionMessage() {}
    
    public CursorPositionMessage(String roomId, String userId, String username, 
                                int position, int lineNumber, int column, String color) {
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.position = position;
        this.lineNumber = lineNumber;
        this.column = column;
        this.color = color;
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
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public int getColumn() {
        return column;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
