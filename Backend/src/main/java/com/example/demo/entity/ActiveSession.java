package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "active_sessions")
public class ActiveSession {
    
    @Id
    @Column(name = "session_id", length = 100)
    private String sessionId;
    
    @Column(name = "user_id", nullable = false, length = 320)
    private String userId;
    
    @Column(name = "room_id", nullable = false, length = 50)
    private String roomId;
    
    @Column(name = "connected_at")
    private LocalDateTime connectedAt;
    
    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;
    
    @Column(name = "cursor_position")
    private int cursorPosition = 0;
    
    // Constructors
    public ActiveSession() {
        this.connectedAt = LocalDateTime.now();
        this.lastHeartbeat = LocalDateTime.now();
    }
    
    public ActiveSession(String sessionId, String userId, String roomId) {
        this();
        this.sessionId = sessionId;
        this.userId = userId;
        this.roomId = roomId;
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
    
    public int getCursorPosition() {
        return cursorPosition;
    }
    
    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }
}
