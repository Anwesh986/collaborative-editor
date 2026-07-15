package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "active_sessions")
public class ActiveSession {
    
    @Id
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;
    
    @Column(name = "username", nullable = false, length = 100)
    private String username;
    
    @Column(name = "user_color", length = 20)
    private String userColor;
    
    @Column(name = "room_id", length = 50)
    private String roomId = "global";
    
    @Column(name = "connected_at")
    private LocalDateTime connectedAt;
    
    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;
    
    @Column(name = "cursor_line")
    private int cursorLine = 0;
    
    @Column(name = "cursor_column")
    private int cursorColumn = 0;
    
    // Constructors
    public ActiveSession() {
        this.connectedAt = LocalDateTime.now();
        this.lastHeartbeat = LocalDateTime.now();
        this.roomId = "global";
    }
    
    public ActiveSession(String sessionId, String userId, String username) {
        this();
        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
    }
    
    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
    
    public String getUserColor() {
        return userColor;
    }
    
    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }
    
    public void setConnectedAt(LocalDateTime connectedAt) {
        this.connectedAt = connectedAt;
    }
    
    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }
    
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
    
    public int getCursorLine() {
        return cursorLine;
    }
    
    public void setCursorLine(int cursorLine) {
        this.cursorLine = cursorLine;
    }
    
    public int getCursorColumn() {
        return cursorColumn;
    }
    
    public void setCursorColumn(int cursorColumn) {
        this.cursorColumn = cursorColumn;
    }
}
