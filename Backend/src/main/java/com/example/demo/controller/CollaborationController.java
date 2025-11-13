package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.ActiveSession;
import com.example.demo.service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CollaborationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CollaborationService collaborationService;

    /**
     * Handle code changes from clients
     * Client sends to: /app/code-change
     * Server broadcasts to: /topic/room/{roomId}/code-change
     */
    @MessageMapping("/code-change")
    public void handleCodeChange(@Payload CodeChangeMessage message) {
        try {
            // Save the updated code to database
            // For now, we'll handle full code updates; incremental updates can be added later
            if ("replace".equals(message.getChangeType())) {
                collaborationService.saveRoomCode(
                    message.getRoomId(),
                    message.getContent(),
                    null, // Keep existing language
                    message.getUserId()
                );
            }

            // Broadcast the change to all other users in the room
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/code-change",
                message
            );

            System.out.println("Code change broadcasted for room: " + message.getRoomId() 
                + " by user: " + message.getUsername());

        } catch (Exception e) {
            System.err.println("Error handling code change: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle cursor position updates
     * Client sends to: /app/cursor-position
     * Server broadcasts to: /topic/room/{roomId}/cursor-position
     */
    @MessageMapping("/cursor-position")
    public void handleCursorPosition(@Payload CursorPositionMessage message) {
        try {
            // Update cursor position in database
            // Note: sessionId would need to be passed or tracked separately
            
            // Broadcast cursor position to all users in the room
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/cursor-position",
                message
            );

        } catch (Exception e) {
            System.err.println("Error handling cursor position: " + e.getMessage());
        }
    }

    /**
     * Handle user joining a room
     * Client sends to: /app/join-room
     * Server broadcasts to: /topic/room/{roomId}/user-joined
     */
    @MessageMapping("/join-room")
    public void handleUserJoined(@Payload UserJoinedMessage message) {
        try {
            // Create session for the user
            String sessionId = message.getRoomId() + "_" + message.getUserId() + "_" + System.currentTimeMillis();
            collaborationService.createSession(sessionId, message.getUserId(), message.getRoomId());

            // Get active user count
            List<ActiveSession> activeSessions = collaborationService.getActiveSessions(message.getRoomId());
            message.setActiveUserCount(activeSessions.size());

            // Broadcast user joined event
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/user-joined",
                message
            );

            // Send system chat message about user joining
            ChatMessage systemMessage = new ChatMessage(
                message.getRoomId(),
                "system",
                "System",
                message.getUsername() + " joined the room",
                "system"
            );
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/chat",
                systemMessage
            );

            // Send full sync to the joining user
            SyncResponseMessage syncResponse = collaborationService.getFullSync(message.getRoomId());
            messagingTemplate.convertAndSendToUser(
                message.getUserId(),
                "/queue/sync",
                syncResponse
            );

            System.out.println("User " + message.getUsername() + " joined room: " + message.getRoomId());

        } catch (Exception e) {
            System.err.println("Error handling user joined: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle user leaving a room
     * Client sends to: /app/leave-room
     * Server broadcasts to: /topic/room/{roomId}/user-left
     */
    @MessageMapping("/leave-room")
    public void handleUserLeft(@Payload UserJoinedMessage message) {
        try {
            // Remove session - we'll need to track sessionId better
            // For now, we'll just broadcast the event
            
            message.setAction("left");
            
            // Get active user count (before removing)
            List<ActiveSession> activeSessions = collaborationService.getActiveSessions(message.getRoomId());
            message.setActiveUserCount(Math.max(0, activeSessions.size() - 1));

            // Broadcast user left event
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/user-left",
                message
            );

            // Send system chat message about user leaving
            ChatMessage systemMessage = new ChatMessage(
                message.getRoomId(),
                "system",
                "System",
                message.getUsername() + " left the room",
                "system"
            );
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/chat",
                systemMessage
            );

            System.out.println("User " + message.getUsername() + " left room: " + message.getRoomId());

        } catch (Exception e) {
            System.err.println("Error handling user left: " + e.getMessage());
        }
    }

    /**
     * Handle sync request (when user reconnects or needs full state)
     * Client sends to: /app/sync-request
     * Server responds to: /user/{userId}/queue/sync
     */
    @MessageMapping("/sync-request")
    public void handleSyncRequest(@Payload SyncRequestMessage message) {
        try {
            // Get full room state
            SyncResponseMessage syncResponse = collaborationService.getFullSync(message.getRoomId());

            // Send sync data back to requesting user
            messagingTemplate.convertAndSendToUser(
                message.getUserId(),
                "/queue/sync",
                syncResponse
            );

            System.out.println("Sync requested by user: " + message.getUsername() 
                + " for room: " + message.getRoomId());

        } catch (Exception e) {
            System.err.println("Error handling sync request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle language change
     */
    @MessageMapping("/language-change")
    public void handleLanguageChange(@Payload LanguageChangeMessage message) {
        try {
            // Update language in database
            collaborationService.saveRoomCode(
                message.getRoomId(),
                null, // Keep existing code
                message.getLanguage(),
                message.getUserId()
            );

            // Broadcast language change to all users
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/language-change",
                message
            );

            System.out.println("Language changed to " + message.getLanguage() 
                + " in room: " + message.getRoomId());

        } catch (Exception e) {
            System.err.println("Error handling language change: " + e.getMessage());
        }
    }

    /**
     * Handle chat messages
     * Client sends to: /app/chat-message
     * Server broadcasts to: /topic/room/{roomId}/chat
     */
    @MessageMapping("/chat-message")
    public void handleChatMessage(@Payload ChatMessage message) {
        try {
            // Broadcast chat message to all users in the room
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/chat",
                message
            );

            System.out.println("💬 Chat message from " + message.getUsername() 
                + " in room " + message.getRoomId() + ": " + message.getMessage());

        } catch (Exception e) {
            System.err.println("Error handling chat message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle terminal output sharing
     * Client sends to: /app/terminal-output
     * Server broadcasts to: /topic/room/{roomId}/terminal-output
     */
    @MessageMapping("/terminal-output")
    public void handleTerminalOutput(@Payload TerminalOutputMessage message) {
        try {
            // Broadcast terminal output to all users in the room
            messagingTemplate.convertAndSend(
                "/topic/room/" + message.getRoomId() + "/terminal-output",
                message
            );

            System.out.println("🖥️  Terminal output from " + message.getUsername() 
                + " in room " + message.getRoomId() + " (" + message.getLanguage() + ")");

        } catch (Exception e) {
            System.err.println("Error handling terminal output: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle WebRTC signaling messages for voice communication
     * Client sends to: /app/webrtc-signal
     * Server routes to: 
     *   - /topic/room/{roomId}/webrtc (broadcast)
     *   - /user/{userId}/queue/webrtc (direct)
     */
    @MessageMapping("/webrtc-signal")
    public void handleWebRTCSignal(@Payload WebRTCSignalMessage message) {
        try {
            if (message.getToUserId() != null && !message.getToUserId().isEmpty()) {
                // Direct message to specific user
                messagingTemplate.convertAndSendToUser(
                    message.getToUserId(),
                    "/queue/webrtc",
                    message
                );
                System.out.println("🎤 WebRTC signal (" + message.getSignalType() + ") from " 
                    + message.getFromUsername() + " to " + message.getToUserId());
            } else {
                // Broadcast to all users in room
                messagingTemplate.convertAndSend(
                    "/topic/room/" + message.getRoomId() + "/webrtc",
                    message
                );
                System.out.println("🎤 WebRTC signal (" + message.getSignalType() + ") from " 
                    + message.getFromUsername() + " broadcasted to room " + message.getRoomId());
            }

        } catch (Exception e) {
            System.err.println("Error handling WebRTC signal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
