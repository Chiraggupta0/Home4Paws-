package com.home4paws.home4paws.service;

import com.home4paws.home4paws.config.ChatSseManager;
import com.home4paws.home4paws.model.AdoptionRequest;
import com.home4paws.home4paws.model.ChatMessage;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.AdoptionRequestRepository;
import com.home4paws.home4paws.repository.ChatMessageRepository;
import com.home4paws.home4paws.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatRepo;
    private final AdoptionRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final ChatSseManager sseManager;
    private final StringRedisTemplate redis;

    public ChatService(ChatMessageRepository chatRepo,
            AdoptionRequestRepository requestRepo,
            UserRepository userRepo,
            ChatSseManager sseManager,
            StringRedisTemplate redis) {
        this.chatRepo = chatRepo;
        this.requestRepo = requestRepo;
        this.userRepo = userRepo;
        this.sseManager = sseManager;
        this.redis = redis;
    }

    public List<ChatMessage> getHistory(Long requestId) {
        return chatRepo.findByAdoptionRequestIdOrderBySentAtAsc(requestId);
    }

    public ChatMessage sendMessage(Long requestId, String senderEmail, String content) {
        AdoptionRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User sender = userRepo.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only adopter or shelter of this request can chat
        boolean isAdopter = request.getAdopter().getEmail().equals(senderEmail);
        boolean isShelter = request.getPet().getShelter().getEmail().equals(senderEmail);
        if (!isAdopter && !isShelter) {
            log.warn("Unauthorized chat attempt on request id={} by {}", requestId, senderEmail);
            throw new RuntimeException("Not authorized");
        }

        ChatMessage msg = new ChatMessage();
        msg.setAdoptionRequest(request);
        msg.setSender(sender);
        msg.setContent(content);
        chatRepo.save(msg);

        // Build a simple DTO to broadcast (avoid lazy-load issues)
        Map<String, Object> dto = Map.of(
                "id", msg.getId(),
                "content", msg.getContent(),
                "sentAt", msg.getSentAt().toString(),
                "senderName", sender.getName(),
                "senderEmail", sender.getEmail());
        String json = sseManager.toJson(dto);
        redis.convertAndSend("chat", requestId + "::" + json);
        log.debug("Chat message id={} sent on request id={} by {}", msg.getId(), requestId, senderEmail);
        return msg;
    }
}
