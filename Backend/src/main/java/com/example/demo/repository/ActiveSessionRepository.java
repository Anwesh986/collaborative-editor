package com.example.demo.repository;

import com.example.demo.entity.ActiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActiveSessionRepository extends JpaRepository<ActiveSession, String> {
    
    List<ActiveSession> findByRoomId(String roomId);
    
    Optional<ActiveSession> findBySessionId(String sessionId);
    
    @Transactional
    @Modifying
    @Query("UPDATE ActiveSession a SET a.lastHeartbeat = :timestamp WHERE a.sessionId = :sessionId")
    void updateHeartbeat(String sessionId, LocalDateTime timestamp);
    
    @Transactional
    @Modifying
    @Query("UPDATE ActiveSession a SET a.cursorPosition = :position WHERE a.sessionId = :sessionId")
    void updateCursorPosition(String sessionId, int position);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM ActiveSession a WHERE a.lastHeartbeat < :timeout")
    void deleteInactiveSessions(LocalDateTime timeout);
}
