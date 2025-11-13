package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Send registration verification email
     */
    public boolean sendRegistrationVerificationEmail(String email, String firstName, String verificationToken) {
        try {
            String verificationLink = "http://localhost:8080/api/registration/verify?token=" + verificationToken;
            
            // Mock registration verification email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Complete Your Registration");
            message.setText("Dear " + firstName + ",\n\nThank you for registering! Please click the link below to verify your email:\n" + verificationLink + "\n\nAfter verification, you will be able to set your password.\nThis link will expire in 24 hours.");
            mailSender.send(message);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send registration verification email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Send verification email to user
     * For now, this is a mock implementation
     * Later you can integrate with real email services like Gmail SMTP, SendGrid, etc.
     */
    public boolean sendVerificationEmail(String email, String username, String verificationToken) {
        try {
            // Mock email sending logic
            // In real implementation, you would:
            // 1. Configure email templates
            // 2. Use JavaMailSender or third-party email service
            // 3. Send actual email with verification link
            
            String verificationLink = "http://localhost:8080/api/auth/verify?token=" + verificationToken;
            
            // For now, just print to console (for development/testing)
            System.out.println("=== EMAIL SENT ===");
            System.out.println("To: " + email);
            System.out.println("Subject: Email Verification Required");
            System.out.println("Dear " + username + ",");
            System.out.println("Please click the link below to verify your email and complete login:");
            System.out.println(verificationLink);
            System.out.println("This link will expire in 15 minutes.");
            System.out.println("=================");
            
            // Return true to simulate successful email sending
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Send welcome email after successful registration
     */
    public boolean sendWelcomeEmail(String email, String username) {
        try {
            // Mock welcome email
            System.out.println("=== WELCOME EMAIL SENT ===");
            System.out.println("To: " + email);
            System.out.println("Subject: Welcome to Collaborative Editor!");
            System.out.println("Dear " + username + ",");
            System.out.println("Welcome to our platform! Your account has been successfully created.");
            System.out.println("==========================");
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Send password reset email
     */
    public boolean sendPasswordResetEmail(String email, String username, String resetToken) {
        try {
            String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + resetToken;
            
            System.out.println("=== PASSWORD RESET EMAIL SENT ===");
            System.out.println("To: " + email);
            System.out.println("Subject: Password Reset Request");
            System.out.println("Dear " + username + ",");
            System.out.println("Click the link below to reset your password:");
            System.out.println(resetLink);
            System.out.println("This link will expire in 30 minutes.");
            System.out.println("================================");
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            return false;
        }
    }
}
