package com.example.demo.controller;

import com.example.demo.entity.UserRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRoomController {
    
    @Autowired
    private com.example.demo.repository.UserRoomRepository userRoomRepository;

    @GetMapping("/user-rooms/in-transition")
    public ResponseEntity<List<UserRoom>> getInTransitionRooms(@RequestParam String email) {
       List<UserRoom> rooms = userRoomRepository.findByUserIdAndIsActive(email, "T");
       return ResponseEntity.ok(rooms);
    }
}
