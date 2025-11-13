package com.example.demo.service;

import com.example.demo.dto.CodeExecutionRequest;
import com.example.demo.dto.CodeExecutionResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {
    
    private static final int TIMEOUT_SECONDS = 10;
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/code_execution/";
    
    public CodeExecutionResponse executeCode(CodeExecutionRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Create temp directory if it doesn't exist
            Files.createDirectories(Paths.get(TEMP_DIR));
            
            switch (request.getLanguage().toLowerCase()) {
                case "python":
                    return executePython(request, startTime);
                case "java":
                    return executeJava(request, startTime);
                case "cpp":
                case "c++":
                    return executeCpp(request, startTime);
                case "c":
                    return executeC(request, startTime);
                case "javascript":
                case "node":
                    return executeNodeJs(request, startTime);
                default:
                    return CodeExecutionResponse.error(
                        "Unsupported language: " + request.getLanguage(), 
                        System.currentTimeMillis() - startTime
                    );
            }
        } catch (Exception e) {
            return CodeExecutionResponse.error(
                "Execution failed: " + e.getMessage(), 
                System.currentTimeMillis() - startTime
            );
        }
    }
    
    private CodeExecutionResponse executePython(CodeExecutionRequest request, long startTime) {
        try {
            // Create temporary Python file
            String fileName = "temp_" + System.currentTimeMillis() + ".py";
            Path filePath = Paths.get(TEMP_DIR, fileName);
            Files.write(filePath, request.getCode().getBytes());
            
            // Execute Python
            ProcessBuilder pb = new ProcessBuilder("python", filePath.toString());
            return executeProcess(pb, request.getInput(), startTime, filePath);
            
        } catch (Exception e) {
            return CodeExecutionResponse.error(
                "Python execution failed: " + e.getMessage(), 
                System.currentTimeMillis() - startTime
            );
        }
    }
    
    private CodeExecutionResponse executeJava(CodeExecutionRequest request, long startTime) {
        try {
            // Extract class name from code (simple approach)
            String className = extractJavaClassName(request.getCode());
            if (className == null) {
                return CodeExecutionResponse.error(
                    "Could not find public class in Java code", 
                    System.currentTimeMillis() - startTime
                );
            }
            
            // Create temporary Java file
            String fileName = className + ".java";
            Path filePath = Paths.get(TEMP_DIR, fileName);
            Files.write(filePath, request.getCode().getBytes());
            
            // Compile Java
            ProcessBuilder compileBuilder = new ProcessBuilder("javac", filePath.toString());
            Process compileProcess = compileBuilder.start();
            
            if (!compileProcess.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                compileProcess.destroyForcibly();
                return CodeExecutionResponse.error(
                    "Java compilation timed out", 
                    System.currentTimeMillis() - startTime
                );
            }
            
            if (compileProcess.exitValue() != 0) {
                String error = readStream(compileProcess.getErrorStream());
                return CodeExecutionResponse.error(
                    "Compilation failed: " + error, 
                    System.currentTimeMillis() - startTime
                );
            }
            
            // Execute compiled Java
            ProcessBuilder runBuilder = new ProcessBuilder("java", "-cp", TEMP_DIR, className);
            return executeProcess(runBuilder, request.getInput(), startTime, filePath);
            
        } catch (Exception e) {
            return CodeExecutionResponse.error(
                "Java execution failed: " + e.getMessage(), 
                System.currentTimeMillis() - startTime
            );
        }
    }
    
    private CodeExecutionResponse executeCpp(CodeExecutionRequest request, long startTime) {
        try {
            // Create temporary C++ file
            String fileName = "temp_" + System.currentTimeMillis() + ".cpp";
            Path filePath = Paths.get(TEMP_DIR, fileName);
            Files.write(filePath, request.getCode().getBytes());
            
            // Compile C++
            String executableName = "temp_" + System.currentTimeMillis();
            Path executablePath = Paths.get(TEMP_DIR, executableName);
            
            ProcessBuilder compileBuilder = new ProcessBuilder("g++", filePath.toString(), "-o", executablePath.toString());
            Process compileProcess = compileBuilder.start();
            
            if (!compileProcess.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                compileProcess.destroyForcibly();
                return CodeExecutionResponse.error(
                    "C++ compilation timed out", 
                    System.currentTimeMillis() - startTime
                );
            }
            
            if (compileProcess.exitValue() != 0) {
                String error = readStream(compileProcess.getErrorStream());
                return CodeExecutionResponse.error(
                    "Compilation failed: " + error, 
                    System.currentTimeMillis() - startTime
                );
            }
            
            // Execute compiled C++
            ProcessBuilder runBuilder = new ProcessBuilder(executablePath.toString());
            return executeProcess(runBuilder, request.getInput(), startTime, filePath, executablePath);
            
        } catch (Exception e) {
            return CodeExecutionResponse.error(
                "C++ execution failed: " + e.getMessage(), 
                System.currentTimeMillis() - startTime
            );
        }
    }
    
    private CodeExecutionResponse executeC(CodeExecutionRequest request, long startTime) {
        try {
            // Create temporary C file
            String fileName = "temp_" + System.currentTimeMillis() + ".c";
            Path filePath = Paths.get(TEMP_DIR, fileName);
            Files.write(filePath, request.getCode().getBytes());
            
            // Compile C
            String executableName = "temp_" + System.currentTimeMillis();
            Path executablePath = Paths.get(TEMP_DIR, executableName);
            
            ProcessBuilder compileBuilder = new ProcessBuilder("gcc", filePath.toString(), "-o", executablePath.toString());
            Process compileProcess = compileBuilder.start();
            
            if (!compileProcess.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                compileProcess.destroyForcibly();
                return CodeExecutionResponse.error(
                    "C compilation timed out", 
                    System.currentTimeMillis() - startTime
                );
            }
            
            if (compileProcess.exitValue() != 0) {
                String error = readStream(compileProcess.getErrorStream());
                return CodeExecutionResponse.error(
                    "Compilation failed: " + error, 
                    System.currentTimeMillis() - startTime
                );
            }
            
            // Execute compiled C
            ProcessBuilder runBuilder = new ProcessBuilder(executablePath.toString());
            return executeProcess(runBuilder, request.getInput(), startTime, filePath, executablePath);
            
        } catch (Exception e) {
            return CodeExecutionResponse.error(
                "C execution failed: " + e.getMessage(), 
                System.currentTimeMillis() - startTime
            );
        }
    }
    
    private CodeExecutionResponse executeNodeJs(CodeExecutionRequest request, long startTime) {
        try {
            // Create temporary JavaScript file
            String fileName = "temp_" + System.currentTimeMillis() + ".js";
            Path filePath = Paths.get(TEMP_DIR, fileName);
            Files.write(filePath, request.getCode().getBytes());
            
            // Execute with Node.js
            ProcessBuilder pb = new ProcessBuilder("node", filePath.toString());
            return executeProcess(pb, request.getInput(), startTime, filePath);
            
        } catch (Exception e) {
            return CodeExecutionResponse.error(
                "Node.js execution failed: " + e.getMessage(), 
                System.currentTimeMillis() - startTime
            );
        }
    }
    
    private CodeExecutionResponse executeProcess(ProcessBuilder pb, String input, long startTime, Path... filesToCleanup) {
        Process process = null;
        try {
            pb.directory(new File(TEMP_DIR));
            process = pb.start();
            final Process finalProcess = process; // Create final reference for lambda
            
            // Handle input in a separate thread to avoid blocking
            if (input != null && !input.isEmpty()) {
                Thread inputThread = new Thread(() -> {
                    try (PrintWriter writer = new PrintWriter(finalProcess.getOutputStream())) {
                        // Split input by lines for multiple cin statements
                        String[] inputLines = input.split("\\n");
                        for (String line : inputLines) {
                            writer.println(line);
                            writer.flush();
                            // Small delay to ensure input is processed
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        System.err.println("Error providing input: " + e.getMessage());
                    }
                });
                inputThread.start();
            } else {
                // Close input stream immediately if no input is provided
                try {
                    process.getOutputStream().close();
                } catch (Exception e) {
                    // Ignore
                }
            }
            
            // Wait for process to complete with timeout
            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return CodeExecutionResponse.error(
                    "Execution timed out after " + TIMEOUT_SECONDS + " seconds", 
                    System.currentTimeMillis() - startTime
                );
            }
            
            // Read output and error streams
            String output = readStream(process.getInputStream());
            String error = readStream(process.getErrorStream());
            int exitCode = process.exitValue();
            long executionTime = System.currentTimeMillis() - startTime;
            
            return new CodeExecutionResponse(output, error, exitCode, executionTime);
            
        } catch (Exception e) {
            return CodeExecutionResponse.error(
                "Process execution failed: " + e.getMessage(), 
                System.currentTimeMillis() - startTime
            );
        } finally {
            if (process != null) {
                process.destroyForcibly();
            }
            // Cleanup temporary files
            for (Path file : filesToCleanup) {
                try {
                    Files.deleteIfExists(file);
                } catch (Exception e) {
                    // Log but don't fail
                    System.err.println("Failed to cleanup file: " + file);
                }
            }
        }
    }
    
    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }
    
    private String extractJavaClassName(String code) {
        // Simple regex to extract public class name
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("public class ")) {
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length - 1; i++) {
                    if ("class".equals(parts[i])) {
                        String className = parts[i + 1];
                        // Remove any braces or implements/extends
                        if (className.contains("{")) {
                            className = className.substring(0, className.indexOf("{"));
                        }
                        if (className.contains(" ")) {
                            className = className.substring(0, className.indexOf(" "));
                        }
                        return className.trim();
                    }
                }
            }
        }
        return null;
    }
}
