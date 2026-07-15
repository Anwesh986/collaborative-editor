package com.example.demo.service;

import com.example.demo.entity.RoomCode;
import com.example.demo.entity.ActiveSession;
import com.example.demo.repository.RoomCodeRepository;
import com.example.demo.repository.ActiveSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class GlobalCodeService {

    private static final String GLOBAL_ROOM_ID = "global";
    private static final String[] ADJECTIVES = { "Happy", "Cool", "Smart", "Clever", "Swift", "Brave", "Bright", "Calm",
            "Quick", "Eager" };
    private static final String[] ANIMALS = { "Penguin", "Tiger", "Eagle", "Dolphin", "Panda", "Lion", "Fox", "Wolf",
            "Bear", "Owl" };
    private static final String[] COLORS = { "#FF6B6B", "#4ECDC4", "#45B7D1", "#FFA07A", "#98D8C8", "#F7DC6F",
            "#BB8FCE", "#85C1E2", "#F8B88B", "#A9DFBF" };
    private final Map<String, ActiveSession> activeSessions = new ConcurrentHashMap<>();

    @Autowired
    private RoomCodeRepository roomCodeRepository;

    @Autowired
    private ActiveSessionRepository activeSessionRepository;

    private Random random = new Random();


    public RoomCode getGlobalCode() {
        return roomCodeRepository.findById(GLOBAL_ROOM_ID)
                .orElseGet(() -> {
                    RoomCode newCode = new RoomCode();
                    newCode.setRoomId(GLOBAL_ROOM_ID);
                    newCode.setCode("// Welcome to Collaborative Code Editor\n// Start typing...");
                    newCode.setLanguage("javascript");
                    newCode.setLastModifiedAt(LocalDateTime.now());
                    newCode.setLastModifiedBy("System");
                    return roomCodeRepository.save(newCode);
                });
    }

    public RoomCode updateCode(String code, String language, String username) {
        RoomCode roomCode = getGlobalCode();
        roomCode.setCode(code);
        roomCode.setLanguage(language);
        roomCode.setLastModifiedAt(LocalDateTime.now());
        roomCode.setLastModifiedBy(username);
        return roomCodeRepository.save(roomCode);
    }

    public String generateRandomUsername() {
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String animal = ANIMALS[random.nextInt(ANIMALS.length)];
        return adjective + "-" + animal;
    }

    public String getUserColor(String userId) {
        return COLORS[Math.abs(userId.hashCode()) % COLORS.length];
    }
    public ActiveSession addUser(String username) {
        String userId = UUID.randomUUID().toString();
        String userColor = getUserColor(userId);

        ActiveSession session = new ActiveSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserId(userId);
        session.setUsername(username);
        session.setUserColor(userColor);
        session.setRoomId(GLOBAL_ROOM_ID);
        session.setConnectedAt(LocalDateTime.now());
        session.setLastHeartbeat(LocalDateTime.now());
        session.setCursorLine(0);
        session.setCursorColumn(0);

        ActiveSession savedSession = activeSessionRepository.save(session);
        activeSessions.put(userId, savedSession);
        
        return savedSession;
    }

    public void removeUser(String userId) {
        activeSessions.remove(userId);
        activeSessionRepository.deleteById(userId);
    }
    public List<ActiveSession> getActiveUsers() {
        return List.copyOf(activeSessions.values());
    }


    public void updateUserCursor(String userId, int line, int column) {
        ActiveSession session = activeSessions.get(userId);
        if (session != null) {
            session.setCursorLine(line);
            session.setCursorColumn(column);
            session.setLastHeartbeat(LocalDateTime.now());
        }
    }
    public void updateHeartbeat(String userId) {
        ActiveSession session = activeSessions.get(userId);
        if (session != null) {
            session.setLastHeartbeat(LocalDateTime.now());
        }
    }
}
