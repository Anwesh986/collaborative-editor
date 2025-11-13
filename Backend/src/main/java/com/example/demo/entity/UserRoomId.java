package com.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserRoomId implements Serializable {
    private String userId;
    private String roomId;

    // Default constructor
    public UserRoomId() {}

    // Constructor
    public UserRoomId(String userId, String roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    // Getters and Setters
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

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoomId that = (UserRoomId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roomId);
    }
}
