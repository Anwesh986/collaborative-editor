package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.UserRoom;
import com.example.demo.entity.UserRoomId;
import java.util.List;

public interface UserRoomRepository extends JpaRepository<UserRoom, UserRoomId> {
    List<UserRoom> findByUserIdAndIsActive(String userId, String isActive);
}