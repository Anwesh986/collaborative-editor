package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_code")
public class RoomCode {
    
    @Id
    @Column(name = "room_id", length = 50)
    private String roomId;
    
    @Column(columnDefinition = "TEXT")
    private String code;
    
    @Column(length = 20)
    private String language = "javascript";
    
    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;
    
    @Column(name = "last_modified_by", length = 320)
    private String lastModifiedBy;
    
    // Constructors
    public RoomCode() {
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    public RoomCode(String roomId, String code, String language, String lastModifiedBy) {
        this();
        this.roomId = roomId;
        this.code = code;
        this.language = language;
        this.lastModifiedBy = lastModifiedBy;
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
    
    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
    
    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
    
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
