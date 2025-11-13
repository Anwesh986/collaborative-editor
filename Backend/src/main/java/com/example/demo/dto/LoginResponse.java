package com.example.demo.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String verificationToken;
    private boolean requiresVerification;
    
    // Constructors
    public LoginResponse() {}
    
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public LoginResponse(boolean success, String message, String verificationToken, boolean requiresVerification) {
        this.success = success;
        this.message = message;
        this.verificationToken = verificationToken;
        this.requiresVerification = requiresVerification;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getVerificationToken() {
        return verificationToken;
    }
    
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
    
    public boolean isRequiresVerification() {
        return requiresVerification;
    }
    
    public void setRequiresVerification(boolean requiresVerification) {
        this.requiresVerification = requiresVerification;
    }
}
