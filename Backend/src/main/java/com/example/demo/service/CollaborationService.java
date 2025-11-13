package com.example.demo.service;

import com.example.demo.dto.SyncResponseMessage;
import com.example.demo.entity.ActiveSession;
import com.example.demo.entity.RoomCode;
import com.example.demo.repository.ActiveSessionRepository;
import com.example.demo.repository.RoomCodeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CollaborationService {

    @Autowired
    private RoomCodeRepository roomCodeRepository;

    @Autowired
    private ActiveSessionRepository activeSessionRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String[] COLORS = {
        "#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA07A", 
        "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E2"
    };

    /**
     * Get or create room code state
     */
    public RoomCode getRoomCode(String roomId) {
        Optional<RoomCode> existing = roomCodeRepository.findByRoomId(roomId);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new room code with default content
        RoomCode roomCode = new RoomCode();
        roomCode.setRoomId(roomId);
        roomCode.setCode("// Welcome to the collaborative editor!\n// Start coding together...\n");
        roomCode.setLanguage("javascript");
        roomCode.setLastModifiedAt(LocalDateTime.now());
        return roomCodeRepository.save(roomCode);
    }

    /**
     * Save updated code to database
     */
    @Transactional
    public void saveRoomCode(String roomId, String code, String language, String userId) {
        RoomCode roomCode = getRoomCode(roomId);
        roomCode.setCode(code);
        if (language != null) {
            roomCode.setLanguage(language);
        }
        roomCode.setLastModifiedBy(userId);
        roomCode.setLastModifiedAt(LocalDateTime.now());
        roomCodeRepository.save(roomCode);
    }

    /**
     * Register a new active session when user joins room
     */
    @Transactional
    public ActiveSession createSession(String sessionId, String userId, String roomId) {
        ActiveSession session = new ActiveSession(sessionId, userId, roomId);
        return activeSessionRepository.save(session);
    }

    /**
     * Remove session when user leaves room
     */
    @Transactional
    public void removeSession(String sessionId) {
        activeSessionRepository.deleteById(sessionId);
    }

    /**
     * Get all active users in a room
     */
    public List<ActiveSession> getActiveSessions(String roomId) {
        return activeSessionRepository.findByRoomId(roomId);
    }

    /**
     * Update user's cursor position
     */
    @Transactional
    public void updateCursorPosition(String sessionId, int position) {
        activeSessionRepository.updateCursorPosition(sessionId, position);
        activeSessionRepository.updateHeartbeat(sessionId, LocalDateTime.now());
    }

    /**
     * Update heartbeat to keep session alive
     */
    @Transactional
    public void updateHeartbeat(String sessionId) {
        activeSessionRepository.updateHeartbeat(sessionId, LocalDateTime.now());
    }

    /**
     * Clean up inactive sessions (older than 5 minutes)
     */
    @Transactional
    public void cleanupInactiveSessions() {
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(5);
        activeSessionRepository.deleteInactiveSessions(timeout);
    }

    /**
     * Get full sync data for a room (code + active users)
     */
    public SyncResponseMessage getFullSync(String roomId) {
        RoomCode roomCode = getRoomCode(roomId);
        List<ActiveSession> activeSessions = getActiveSessions(roomId);
        
        List<SyncResponseMessage.ActiveUserInfo> activeUsers = new ArrayList<>();
        int colorIndex = 0;
        
        for (ActiveSession session : activeSessions) {
            String username = getUsernameFromId(session.getUserId());
            String color = COLORS[colorIndex % COLORS.length];
            
            SyncResponseMessage.ActiveUserInfo userInfo = new SyncResponseMessage.ActiveUserInfo(
                session.getUserId(),
                username,
                color,
                session.getCursorPosition()
            );
            activeUsers.add(userInfo);
            colorIndex++;
        }
        
        return new SyncResponseMessage(
            roomId,
            roomCode.getCode(),
            roomCode.getLanguage(),
            0, // Version (to be implemented later)
            activeUsers
        );
    }

    /**
     * Get username from user ID (email)
     */
    private String getUsernameFromId(String userId) {
        return userRepository.findByEmail(userId)
            .map(user -> user.getUsername())
            .orElse("Unknown User");
    }

    /**
     * Assign a color to a user
     */
    public String assignUserColor(String roomId) {
        List<ActiveSession> activeSessions = getActiveSessions(roomId);
        int colorIndex = activeSessions.size() % COLORS.length;
        return COLORS[colorIndex];
    }
}
