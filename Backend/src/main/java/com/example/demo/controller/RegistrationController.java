package com.example.demo.controller;

import com.example.demo.dto.RegistrationRequest;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.SetPasswordRequest;
import com.example.demo.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    /**
     * Step 1: Initial Registration (FirstName, LastName, Email)
     * POST /api/registration/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        
        // Input validation
        if (registrationRequest.getEmail() == null || registrationRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Email is required"));
        }
        
        if (registrationRequest.getFirstName() == null || registrationRequest.getFirstName().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("First name is required"));
        }
        
        if (registrationRequest.getLastName() == null || registrationRequest.getLastName().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Last name is required"));
        }
        
        // Call business logic
        ApiResponse response = registrationService.registerUser(registrationRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Step 2: Email Verification 
     * GET /api/registration/verify?token=xyz
     */
    @GetMapping("/verify")
    public ResponseEntity<Void> verifyEmail(@RequestParam("token") String token) {
        
        if (token == null || token.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Call business logic
        ResponseEntity<Void> response = registrationService.verifyEmailForRegistration(token);

        if (response.getStatusCode() == HttpStatus.FOUND) {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", response.getHeaders().getLocation().toString()).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Step 3: Set Password and Complete Registration
     * POST /api/registration/set-password
     */
    @PostMapping("/set-password")
    public ResponseEntity<ApiResponse> setPassword(@RequestBody SetPasswordRequest setPasswordRequest) {
        
        // Input validation
        if (setPasswordRequest.getVerificationToken() == null || setPasswordRequest.getVerificationToken().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Verification token is required"));
        }
        
        if (setPasswordRequest.getPassword() == null || setPasswordRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Password is required"));
        }
        
        if (setPasswordRequest.getConfirmPassword() == null || setPasswordRequest.getConfirmPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Confirm password is required"));
        }
        
        // Call business logic
        ApiResponse response = registrationService.setPassword(setPasswordRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Test endpoint
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Registration API is working!");
    }
}
