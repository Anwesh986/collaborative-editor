package com.example.demo.service;

import com.example.demo.dto.CodeExecutionRequest;
import com.example.demo.dto.CodeExecutionResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
public class CodeExecutionService {

    private static final int TIMEOUT_SECONDS = 10;
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/code_execution/";

    public CodeExecutionResponse executeCode(CodeExecutionRequest request) {
        long startTime = System.currentTimeMillis();
        
        // Generate a unique directory for this specific execution to prevent file collisions
        String executionId = UUID.randomUUID().toString();
        Path executionDir = Paths.get(TEMP_DIR, executionId);

        try {
            // Create the isolated temp directory
            Files.createDirectories(executionDir);

            switch (request.getLanguage().toLowerCase()) {
                case "python":
                    return executePython(request, startTime, executionDir);
                case "java":
                    return executeJava(request, startTime, executionDir);
                case "cpp":
                case "c++":
                    return executeCpp(request, startTime, executionDir);
                case "c":
                    return executeC(request, startTime, executionDir);
                case "javascript":
                case "node":
                    return executeNodeJs(request, startTime, executionDir);
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
        } finally {
            // Always clean up the isolated directory when finished
            cleanupDirectory(executionDir);
        }
    }

    private CodeExecutionResponse executePython(CodeExecutionRequest request, long startTime, Path executionDir) {
        try {
            String fileName = "main.py";
            Path filePath = executionDir.resolve(fileName);
            Files.write(filePath, request.getCode().getBytes());

            ProcessBuilder pb = new ProcessBuilder("python", filePath.toString());
            return executeProcess(pb, request.getInput(), startTime, executionDir);

        } catch (Exception e) {
            return CodeExecutionResponse.error(
                    "Python execution failed: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            );
        }
    }

    private CodeExecutionResponse executeJava(CodeExecutionRequest request, long startTime, Path executionDir) {
        try {
            String className = extractJavaClassName(request.getCode());
            if (className == null) {
                return CodeExecutionResponse.error(
                        "Could not find public class in Java code",
                        System.currentTimeMillis() - startTime
                    );
            }

            String fileName = className + ".java";
            Path filePath = executionDir.resolve(fileName);
            Files.write(filePath, request.getCode().getBytes());

            ProcessBuilder compileBuilder = new ProcessBuilder("javac", filePath.toString());
            compileBuilder.directory(executionDir.toFile());
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

            ProcessBuilder runBuilder = new ProcessBuilder("java", "-cp", executionDir.toString(), className);
            return executeProcess(runBuilder, request.getInput(), startTime, executionDir);

        } catch (Exception e) {
            return CodeExecutionResponse.error(
                    "Java execution failed: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            );
        }
    }

    private CodeExecutionResponse executeCpp(CodeExecutionRequest request, long startTime, Path executionDir) {
        try {
            String fileName = "main.cpp";
            Path filePath = executionDir.resolve(fileName);
            Files.write(filePath, request.getCode().getBytes());

            String executableName = "program";
            Path executablePath = executionDir.resolve(executableName);

            ProcessBuilder compileBuilder = new ProcessBuilder("g++", filePath.toString(), "-o", executablePath.toString());
            compileBuilder.directory(executionDir.toFile());
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

            ProcessBuilder runBuilder = new ProcessBuilder(executablePath.toString());
            return executeProcess(runBuilder, request.getInput(), startTime, executionDir);

        } catch (Exception e) {
            return CodeExecutionResponse.error(
                    "C++ execution failed: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            );
        }
    }

    private CodeExecutionResponse executeC(CodeExecutionRequest request, long startTime, Path executionDir) {
        try {
            String fileName = "main.c";
            Path filePath = executionDir.resolve(fileName);
            Files.write(filePath, request.getCode().getBytes());

            String executableName = "program";
            Path executablePath = executionDir.resolve(executableName);

            ProcessBuilder compileBuilder = new ProcessBuilder("gcc", filePath.toString(), "-o", executablePath.toString());
            compileBuilder.directory(executionDir.toFile());
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

            ProcessBuilder runBuilder = new ProcessBuilder(executablePath.toString());
            return executeProcess(runBuilder, request.getInput(), startTime, executionDir);

        } catch (Exception e) {
            return CodeExecutionResponse.error(
                    "C execution failed: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            );
        }
    }

    private CodeExecutionResponse executeNodeJs(CodeExecutionRequest request, long startTime, Path executionDir) {
        try {
            String fileName = "main.js";
            Path filePath = executionDir.resolve(fileName);
            Files.write(filePath, request.getCode().getBytes());

            ProcessBuilder pb = new ProcessBuilder("node", filePath.toString());
            return executeProcess(pb, request.getInput(), startTime, executionDir);

        } catch (Exception e) {
            return CodeExecutionResponse.error(
                    "Node.js execution failed: " + e.getMessage(),
                    System.currentTimeMillis() - startTime
            );
        }
    }

    private CodeExecutionResponse executeProcess(ProcessBuilder pb, String input, long startTime, Path executionDir) {
        Process process = null;
        try {
            // Set the working directory to the isolated folder
            pb.directory(executionDir.toFile());
            process = pb.start();
            final Process finalProcess = process;

            if (input != null && !input.isEmpty()) {
                Thread inputThread = new Thread(() -> {
                    try (PrintWriter writer = new PrintWriter(finalProcess.getOutputStream())) {
                        String[] inputLines = input.split("\\n");
                        for (String line : inputLines) {
                            writer.println(line);
                            writer.flush();
                            Thread.sleep(10);
                        }
                    } catch (Exception e) {
                        System.err.println("Error providing input: " + e.getMessage());
                    }
                });
                inputThread.start();
            } else {
                try {
                    process.getOutputStream().close();
                } catch (Exception e) {
                    // Ignore
                }
            }

            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                return CodeExecutionResponse.error(
                        "Execution timed out after " + TIMEOUT_SECONDS + " seconds",
                        System.currentTimeMillis() - startTime
                );
            }

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
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("public class ")) {
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length - 1; i++) {
                    if ("class".equals(parts[i])) {
                        String className = parts[i + 1];
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

    // Helper method to completely remove the execution directory and its contents
    private void cleanupDirectory(Path dir) {
        if (Files.exists(dir)) {
            try (Stream<Path> walk = Files.walk(dir)) {
                walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            } catch (IOException e) {
                System.err.println("Failed to clean up directory: " + dir);
            }
        }
    }
}