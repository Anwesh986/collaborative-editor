package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class GlobalCollaborationController {

    @MessageMapping("/editor/chat")
    @SendTo("/topic/editor/chat")
    public Map<String, Object> handleChat(Map<String, Object> message) {
        return message; // Broadcasts the chat message to all connected clients
    }

    @MessageMapping("/editor/cursor")
    @SendTo("/topic/editor/cursor")
    public Map<String, Object> handleCursor(Map<String, Object> cursor) {
        return cursor; // Broadcasts cursor position
    }

    @MessageMapping("/editor/code-change")
    @SendTo("/topic/editor/code-change")
    public Map<String, Object> handleCodeChange(Map<String, Object> change) {
        return change; // Broadcasts code updates
    }

    @MessageMapping("/editor/language-change")
    @SendTo("/topic/editor/language-change")
    public Map<String, Object> handleLanguageChange(Map<String, Object> change) {
        return change; // Broadcasts dropdown changes
    }
    
    @MessageMapping("/editor/init")
    @SendTo("/topic/editor/init-response")
    public Map<String, Object> handleInit(Map<String, Object> payload) {
        return payload; // Acknowledges a new user joined
    }

    @MessageMapping("/editor/input")
    @SendTo("/topic/editor/input")
    public Map<String, Object> handleInput(Map<String, Object> input) {
        return input; // Broadcasts the program input typing
    }

    // --- ADD THIS MISSING METHOD ---
    @MessageMapping("/editor/terminal")
    @SendTo("/topic/editor/terminal")
    public Map<String, Object> handleTerminal(Map<String, Object> output) {
        return output; // Broadcasts the execution results 
    }
}