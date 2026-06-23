package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.config.ChatSseManager;
import com.home4paws.home4paws.model.ChatMessage;
import com.home4paws.home4paws.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatSseManager sseManager;

    public ChatController(ChatService chatService, ChatSseManager sseManager) {
        this.chatService = chatService;
        this.sseManager  = sseManager;
    }

    // Load message history
    @GetMapping("/{requestId}")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable Long requestId) {
        return ResponseEntity.ok(chatService.getHistory(requestId));
    }

    // Send a message
    @PostMapping("/{requestId}")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> body,
            Principal principal) {
        chatService.sendMessage(requestId, principal.getName(), body.get("content"));
        return ResponseEntity.ok().build();
    }

    // SSE stream — browser connects here to receive live messages
    @GetMapping(value = "/stream/{requestId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable Long requestId) {
        return sseManager.subscribe(requestId);
    }
}
