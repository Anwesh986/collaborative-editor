package com.example.demo.dto;

public class WebRTCSignalMessage {
    private String roomId;
    private String fromUserId;
    private String fromUsername;
    private String toUserId; // null for broadcast, specific userId for direct message
    private String signalType; // "offer", "answer", "ice-candidate", "join-audio", "leave-audio"
    private Object signalData; // SDP offer/answer or ICE candidate
    private long timestamp;
    
    // Constructors
    public WebRTCSignalMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public WebRTCSignalMessage(String roomId, String fromUserId, String fromUsername, 
                               String toUserId, String signalType, Object signalData) {
        this();
        this.roomId = roomId;
        this.fromUserId = fromUserId;
        this.fromUsername = fromUsername;
        this.toUserId = toUserId;
        this.signalType = signalType;
        this.signalData = signalData;
    }
    
    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public String getFromUserId() {
        return fromUserId;
    }
    
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
    
    public String getFromUsername() {
        return fromUsername;
    }
    
    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }
    
    public String getToUserId() {
        return toUserId;
    }
    
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
    
    public String getSignalType() {
        return signalType;
    }
    
    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }
    
    public Object getSignalData() {
        return signalData;
    }
    
    public void setSignalData(Object signalData) {
        this.signalData = signalData;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
