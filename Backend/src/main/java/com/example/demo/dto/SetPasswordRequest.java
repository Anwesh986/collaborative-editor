package com.example.demo.dto;

public class SetPasswordRequest {
    private String verificationToken;
    private String password;
    private String confirmPassword;
    
    // Constructors
    public SetPasswordRequest() {}
    
    public SetPasswordRequest(String verificationToken, String password, String confirmPassword) {
        this.verificationToken = verificationToken;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    
    // Getters and Setters
    public String getVerificationToken() {
        return verificationToken;
    }
    
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
