package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RoomService {
    public boolean isUserActiveInRoom(String userId, String roomId) {
        String sql = "SELECT COUNT(*) FROM user_rooms WHERE user_id = ? AND room_id = ? AND is_active = 'Y'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, roomId);
        return count != null && count > 0;
    }

    public boolean roomExists(String roomId) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, roomId);
        return count != null && count > 0;
    }

    public void addUserToRoom(String userId, String roomId) {
        String sql = "INSERT INTO user_rooms (user_id, room_id, is_active) VALUES (?, ?, 'Y')";
        jdbcTemplate.update(sql, userId, roomId);
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createRoomWithUsers(String roomId, String roomName, String createdBy, List<String> userEmails) {
        // Insert into rooms table
        jdbcTemplate.update(
            "INSERT INTO rooms (id, name, created_by) VALUES (?, ?, ?)",
            roomId, roomName, createdBy
        );
        jdbcTemplate.update(
                "INSERT INTO user_rooms (user_id, room_id, is_active) VALUES (?, ?, ?)",
                createdBy, roomId, "Y"
            );
        // Insert into user_rooms table
        for (String userEmail : userEmails) {
            jdbcTemplate.update(
                "INSERT INTO user_rooms (user_id, room_id, is_active) VALUES (?, ?, ?)",
                userEmail, roomId, "T"
            );
        }
    }
}
