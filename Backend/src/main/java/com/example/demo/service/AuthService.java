package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Main login business logic
     * Simplified login without email verification
     */
    public ApiResponse authenticateUser(LoginRequest loginRequest) {
        try {
            // Step 1: Find user by email
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            
            if (userOptional.isEmpty()) {
                return ApiResponse.error("User not found with this email");
            }
            
            User user = userOptional.get();
            
            // Step 2: Verify password
            if (!verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                return ApiResponse.error("Invalid password");
            }
            
            // Step 3: Update last login time
            userRepository.updateLastLogin(user.getEmail(), LocalDateTime.now());
            
            // Step 4: Create user data map
            java.util.Map<String, Object> userData = new java.util.HashMap<>();
            userData.put("email", user.getEmail());
            userData.put("username", user.getUsername());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            
            // Step 5: Return success response with user data
            return ApiResponse.success("Login successful! Welcome back, " + user.getUsername(), userData);
            
        } catch (Exception e) {
            // Log the error (you can add proper logging later)
            System.err.println("Login error: " + e.getMessage());
            return ApiResponse.error("An error occurred during login. Please try again.");
        }
    }

    /**
     * Password encryption - converts plain text to encrypted bytes
     */
    public byte[] encryptPassword(String plainPassword) {
        try {
            // Generate salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            // Create hash with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(plainPassword.getBytes());
            
            // Combine salt + hash
            byte[] result = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, result, 0, salt.length);
            System.arraycopy(hashedPassword, 0, result, salt.length, hashedPassword.length);
            
            return result;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password encryption failed", e);
        }
    }

    /**
     * Password verification - compares plain text with encrypted bytes
     */
    private boolean verifyPassword(String plainPassword, byte[] storedPassword) {
        try {
            // Extract salt (first 16 bytes)
            byte[] salt = Arrays.copyOfRange(storedPassword, 0, 16);
            
            // Extract hash (remaining bytes)
            byte[] storedHash = Arrays.copyOfRange(storedPassword, 16, storedPassword.length);
            
            // Hash the provided password with the same salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] testHash = md.digest(plainPassword.getBytes());
            
            // Compare hashes
            return Arrays.equals(storedHash, testHash);
            
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Password verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clean up expired verification tokens (utility method)
     * Keep this for future use or registration process
     */
    public void cleanupExpiredTokens() {
        userRepository.cleanupExpiredTokens(LocalDateTime.now());
    }
}
