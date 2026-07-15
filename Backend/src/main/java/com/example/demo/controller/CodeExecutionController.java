package com.example.demo.controller;

import com.example.demo.dto.CodeExecutionRequest;
import com.example.demo.dto.CodeExecutionResponse;
import com.example.demo.service.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
public class CodeExecutionController {
    
    @Autowired
    private CodeExecutionService codeExecutionService;
    
    @PostMapping("/execute")
    public ResponseEntity<CodeExecutionResponse> executeCode(@RequestBody CodeExecutionRequest request) {
        try {
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(CodeExecutionResponse.error("Code cannot be empty", 0));
            }
            
            if (request.getLanguage() == null || request.getLanguage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(CodeExecutionResponse.error("Language must be specified", 0));
            }
            
            // Execute code
            CodeExecutionResponse response = codeExecutionService.executeCode(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(CodeExecutionResponse.error("Internal server error: " + e.getMessage(), 0));
        }
    }
    
    @GetMapping("/execute/languages")
    public ResponseEntity<String[]> getSupportedLanguages() {
        String[] languages = {
            "python",
            "java", 
            "cpp",
            "c++",
            "c",
            "javascript",
            "node"
        };
        return ResponseEntity.ok(languages);
    }
    
    @GetMapping("/execute/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Code execution service is running");
    }
}
