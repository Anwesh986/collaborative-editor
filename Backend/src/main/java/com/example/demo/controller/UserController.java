package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // Get all users, optionally filter by email
    @GetMapping
    public List<User> getAllUsers(@RequestParam(value = "email", required = false) String email) {
        if (email != null && !email.isEmpty()) {
            return userRepository.findAll().stream()
                .filter(user -> user.getEmail().toLowerCase().contains(email.toLowerCase()))
                .toList();
        }
        return userRepository.findAll();
    }
}
