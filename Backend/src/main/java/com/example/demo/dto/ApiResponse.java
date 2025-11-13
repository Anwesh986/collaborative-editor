package com.example.demo.dto;

public class ApiResponse {
    private boolean success;
    private String message;
    private String token;
    private String redirectUrl;
    private Object data; // For any additional data
    
    // Constructors
    public ApiResponse() {}
    
    // Basic response (success/failure with message)
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Response with token
    public ApiResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }
    
    // Full response with all fields
    public ApiResponse(boolean success, String message, String token, String redirectUrl) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.redirectUrl = redirectUrl;
    }
    
    // Response with additional data
    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    // Static factory methods for common responses
    public static ApiResponse success(String message) {
        return new ApiResponse(true, message);
    }
    
    public static ApiResponse success(String message, String token) {
        return new ApiResponse(true, message, token);
    }
    
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }
    
    public static ApiResponse error(String message) {
        return new ApiResponse(false, message);
    }
    
    public static ApiResponse redirect(String message, String redirectUrl) {
        return new ApiResponse(true, message, null, redirectUrl);
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
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
}
