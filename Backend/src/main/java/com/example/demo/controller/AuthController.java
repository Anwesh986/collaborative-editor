package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ApiResponse;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Login endpoint - receives LoginRequest DTO from frontend
     * This is where your frontend will send the login data
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        
        // Input validation
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Email is required"));
        }
        
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Password is required"));
        }
        
        // Call business logic in AuthService
        ApiResponse response = authService.authenticateUser(loginRequest);
        
        // Return appropriate HTTP status based on success
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Test endpoint to check if auth API is working
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth API is working!");
    }

    /**
     * Cleanup endpoint (for maintenance)
     * Removes expired verification tokens
     */
    @PostMapping("/cleanup")
    public ResponseEntity<String> cleanup() {
        authService.cleanupExpiredTokens();
        return ResponseEntity.ok("Expired tokens cleaned up successfully");
    }
}
