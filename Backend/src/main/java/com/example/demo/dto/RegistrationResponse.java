package com.example.demo.dto;

public class RegistrationResponse {
    private boolean success;
    private String message;
    private String verificationToken;
    private String redirectUrl;
    
    // Constructors
    public RegistrationResponse() {}
    
    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public RegistrationResponse(boolean success, String message, String verificationToken, String redirectUrl) {
        this.success = success;
        this.message = message;
        this.verificationToken = verificationToken;
        this.redirectUrl = redirectUrl;
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
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
