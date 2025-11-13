package com.example.demo.service;

import com.example.demo.dto.RegistrationRequest;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.SetPasswordRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AuthService authService;

    /**
     * Step 1: Initial Registration (FirstName, LastName, Email)
     * This creates a temporary user record WITHOUT email verification (for LAN environments)
     */
    public ApiResponse registerUser(RegistrationRequest registrationRequest) {
        try {
            // Step 1: Validate input
            if (registrationRequest.getEmail() == null || registrationRequest.getEmail().trim().isEmpty()) {
                return ApiResponse.error("Email is required");
            }
            
            if (registrationRequest.getFirstName() == null || registrationRequest.getFirstName().trim().isEmpty()) {
                return ApiResponse.error("First name is required");
            }
            
            if (registrationRequest.getLastName() == null || registrationRequest.getLastName().trim().isEmpty()) {
                return ApiResponse.error("Last name is required");
            }

            // Step 2: Check if email already exists
            if (userRepository.existsByEmail(registrationRequest.getEmail())) {
                return ApiResponse.error("Email already exists. Please use a different email or try logging in.");
            }

            // Step 3: Generate username from firstName + lastName
            String username = generateUsername(registrationRequest.getFirstName(), registrationRequest.getLastName());
            
            // Step 4: Generate verification token
            String verificationToken = generateVerificationToken();
            LocalDateTime tokenExpiry = LocalDateTime.now().plusHours(24); // 24 hours for registration

            // Step 5: Create temporary user record (without password)
            User tempUser = new User();
            tempUser.setEmail(registrationRequest.getEmail());
            tempUser.setFirstName(registrationRequest.getFirstName());
            tempUser.setLastName(registrationRequest.getLastName());
            tempUser.setUsername(username);
            tempUser.setVerified(false);
            tempUser.setVerificationToken(verificationToken);
            tempUser.setVerificationTokenExpiry(tokenExpiry);
            tempUser.setCreatedAt(LocalDateTime.now());
            // password remains null until user sets it

            // Step 6: Insert temporary user into database
            userRepository.insertTempUser(
                tempUser.getEmail(),
                tempUser.getFirstName(),
                tempUser.getLastName(),
                tempUser.getUsername(),
                tempUser.isVerified(),
                tempUser.getVerificationToken(),
                tempUser.getVerificationTokenExpiry(),
                tempUser.getCreatedAt()
            );
            
            // Step 7: SKIP EMAIL - Try to send but don't fail if it doesn't work
            try {
                emailService.sendRegistrationVerificationEmail(
                    tempUser.getEmail(),
                    tempUser.getFirstName(),
                    verificationToken
                );
            } catch (Exception emailError) {
                System.err.println("Email sending skipped (LAN environment): " + emailError.getMessage());
            }
            
            // Step 8: Return success response with token for direct password setting
            ApiResponse response = ApiResponse.success(
                "Registration successful! Please set your password to complete registration.",
                verificationToken
            );
            response.setRedirectUrl("/set-password/" + verificationToken);
            return response;

        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            return ApiResponse.error("An error occurred during registration. Please try again.");
        }
    }

    /**
     * Step 2: Verify Email and Show Password Setting Page
     */
    public ResponseEntity<Void> verifyEmailForRegistration(String token) {
        try {
            // Find user by verification token
            Optional<User> userOptional = userRepository.findByVerificationToken(token);

            if (userOptional.isEmpty()) {
                // Redirect to error page or show error (customize as needed)
                return ResponseEntity.status(302).header("Location", "http://localhost:4200/register?error=invalid_token").build();
            }

            User user = userOptional.get();

            // Check if token is expired
            if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(302).header("Location", "http://localhost:4200/register?error=token_expired").build();
            }

            // Check if user already has password set
            if (user.getPassword() != null) {
                return ResponseEntity.status(302).header("Location", "http://localhost:4200/login?error=already_registered").build();
            }
            user.setVerified(true);
            userRepository.save(user);
            // Redirect to set-password page with token
            String redirectUrl = "http://localhost:4200/set-password/" + token;
            return ResponseEntity.status(302).header("Location", redirectUrl).build();

        } catch (Exception e) {
            System.err.println("Email verification error: " + e.getMessage());
            return ResponseEntity.status(302).header("Location", "http://localhost:4200/register?error=server_error").build();
        }
    }

    /**
     * Step 3: Set Password and Complete Registration
     */
    public ApiResponse setPassword(SetPasswordRequest setPasswordRequest) {
        try {
            // Step 1: Validate input
            if (setPasswordRequest.getPassword() == null || setPasswordRequest.getPassword().trim().isEmpty()) {
                return ApiResponse.error("Password is required");
            }

            if (setPasswordRequest.getConfirmPassword() == null || setPasswordRequest.getConfirmPassword().trim().isEmpty()) {
                return ApiResponse.error("Confirm password is required");
            }

            // Step 2: Check if passwords match
            if (!setPasswordRequest.getPassword().equals(setPasswordRequest.getConfirmPassword())) {
                return ApiResponse.error("Passwords do not match");
            }

            // Step 3: Password strength validation
            if (setPasswordRequest.getPassword().length() < 6) {
                return ApiResponse.error("Password must be at least 6 characters long");
            }

            // Step 4: Find user by verification token
            Optional<User> userOptional = userRepository.findByVerificationToken(setPasswordRequest.getVerificationToken());

            if (userOptional.isEmpty()) {
                return ApiResponse.error("Invalid verification token");
            }

            User user = userOptional.get();

            // Step 5: Check if token is expired
            if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
                return ApiResponse.error("Verification token has expired. Please register again.");
            }

            // Step 6: Encrypt password
            byte[] encryptedPassword = authService.encryptPassword(setPasswordRequest.getPassword());

            // Step 7: Update user with password and mark as verified
            userRepository.completeRegistration(
                user.getEmail(),
                encryptedPassword,
                true
            );

            // Step 8: Send welcome email
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

            // Step 9: Return success response
            ApiResponse response = ApiResponse.success("Registration completed successfully! You can now login with your credentials.");
            response.setRedirectUrl("/login");
            return response;

        } catch (Exception e) {
            System.err.println("Set password error: " + e.getMessage());
            return ApiResponse.error("An error occurred while setting password. Please try again.");
        }
    }

    /**
     * Generate username from first and last name
     */
    private String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        
        // Check if username exists, if yes, add numbers
        String username = baseUsername;
        int counter = 1;
        
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }

    /**
     * Generate secure verification token
     */
    private String generateVerificationToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
