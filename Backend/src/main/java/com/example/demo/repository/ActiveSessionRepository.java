package com.example.demo.repository;

import com.example.demo.entity.ActiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActiveSessionRepository extends JpaRepository<ActiveSession, String> {
    
    List<ActiveSession> findByRoomId(String roomId);
    
    Optional<ActiveSession> findBySessionId(String sessionId);
}
