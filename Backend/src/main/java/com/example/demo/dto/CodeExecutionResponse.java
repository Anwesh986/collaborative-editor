package com.example.demo.dto;

public class CodeExecutionResponse {
    private String output;
    private String error;
    private int exitCode;
    private long executionTime;
    private boolean success;
    
    // Constructors
    public CodeExecutionResponse() {}
    
    public CodeExecutionResponse(String output, String error, int exitCode, long executionTime) {
        this.output = output;
        this.error = error;
        this.exitCode = exitCode;
        this.executionTime = executionTime;
        this.success = exitCode == 0 && (error == null || error.isEmpty());
    }
    
    // Static factory methods for common cases
    public static CodeExecutionResponse success(String output, long executionTime) {
        return new CodeExecutionResponse(output, null, 0, executionTime);
    }
    
    public static CodeExecutionResponse error(String error, long executionTime) {
        return new CodeExecutionResponse(null, error, 1, executionTime);
    }
    
    public static CodeExecutionResponse failure(String output, String error, int exitCode, long executionTime) {
        return new CodeExecutionResponse(output, error, exitCode, executionTime);
    }
    
    // Getters and Setters
    public String getOutput() {
        return output;
    }
    
    public void setOutput(String output) {
        this.output = output;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public int getExitCode() {
        return exitCode;
    }
    
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
