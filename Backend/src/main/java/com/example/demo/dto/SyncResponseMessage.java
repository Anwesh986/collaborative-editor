package com.example.demo.dto;

import java.util.List;

public class SyncResponseMessage {
    private String roomId;
    private String code; // Full code content
    private String language;
    private int currentVersion;
    private List<ActiveUserInfo> activeUsers;
    
    // Constructors
    public SyncResponseMessage() {}
    
    public SyncResponseMessage(String roomId, String code, String language, int currentVersion, List<ActiveUserInfo> activeUsers) {
        this.roomId = roomId;
        this.code = code;
        this.language = language;
        this.currentVersion = currentVersion;
        this.activeUsers = activeUsers;
    }
    
    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public int getCurrentVersion() {
        return currentVersion;
    }
    
    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }
    
    public List<ActiveUserInfo> getActiveUsers() {
        return activeUsers;
    }
    
    public void setActiveUsers(List<ActiveUserInfo> activeUsers) {
        this.activeUsers = activeUsers;
    }
    
    // Inner class for active user information
    public static class ActiveUserInfo {
        private String userId;
        private String username;
        private String color;
        private int cursorPosition;
        
        public ActiveUserInfo() {}
        
        public ActiveUserInfo(String userId, String username, String color, int cursorPosition) {
            this.userId = userId;
            this.username = username;
            this.color = color;
            this.cursorPosition = cursorPosition;
        }
        
        // Getters and Setters
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
        
        public String getColor() {
            return color;
        }
        
        public void setColor(String color) {
            this.color = color;
        }
        
        public int getCursorPosition() {
            return cursorPosition;
        }
        
        public void setCursorPosition(int cursorPosition) {
            this.cursorPosition = cursorPosition;
        }
    }
}
