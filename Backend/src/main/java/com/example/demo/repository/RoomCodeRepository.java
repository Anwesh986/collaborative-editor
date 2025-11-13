package com.example.demo.repository;

import com.example.demo.entity.RoomCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomCodeRepository extends JpaRepository<RoomCode, String> {
    Optional<RoomCode> findByRoomId(String roomId);
}
