package com.example.demo.controller;

import com.example.demo.entity.UserRoom;
import com.example.demo.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @PostMapping("/join")
    public ResponseEntity<?> joinRoom(@RequestBody Map<String, String> payload) {
        String roomId = payload.get("roomId");
        String userId = payload.get("userId");
        // Check if user already joined and is active
        if (roomService.isUserActiveInRoom(userId, roomId)) {
            return ResponseEntity.ok(Map.of("status", "already_joined"));
        }
        // Check if room exists
        if (!roomService.roomExists(roomId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "not_found"));
        }
        // Join room
        roomService.addUserToRoom(userId, roomId);
        return ResponseEntity.ok(Map.of("status", "joined"));
    }
    @Autowired
    private RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody Map<String, Object> payload) {
        try {
            String roomId = (String) payload.get("roomId");
            String roomName = (String) payload.get("roomName");
            String createdBy = payload.get("createdBy").toString();
            List<String> userEmails = (List<String>) payload.get("userEmails");
            
            roomService.createRoomWithUsers(roomId, roomName, createdBy, userEmails);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Room created successfully"));
        } catch (Exception e) {
            System.err.println("Error creating room: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
