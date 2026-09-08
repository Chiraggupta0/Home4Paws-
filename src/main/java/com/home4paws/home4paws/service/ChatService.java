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
    private final SubscriptionService subscriptionService;

    public ChatService(ChatMessageRepository chatRepo,
            AdoptionRequestRepository requestRepo,
            UserRepository userRepo,
            ChatSseManager sseManager,
            StringRedisTemplate redis,
            SubscriptionService subscriptionService) {
        this.chatRepo = chatRepo;
        this.requestRepo = requestRepo;
        this.userRepo = userRepo;
        this.sseManager = sseManager;
        this.redis = redis;
        this.subscriptionService = subscriptionService;
    }

    // Pets with a price are SELLER listings — both the buyer and the seller must be
    // subscribed to chat about them. NGO pets (price == null) stay free for everyone.
    private void requireChatAccess(AdoptionRequest request, String requesterEmail) {
        boolean isPaidListing = request.getPet().getPrice() != null;
        if (!isPaidListing) return;

        if (!subscriptionService.isSubscribed(requesterEmail)) {
            throw new RuntimeException("Subscribe to chat on this listing");
        }
    }

    public List<ChatMessage> getHistory(Long requestId, String requesterEmail) {
        AdoptionRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        boolean isAdopter = request.getAdopter().getEmail().equals(requesterEmail);
        boolean isShelter = request.getPet().getShelter().getEmail().equals(requesterEmail);
        if (!isAdopter && !isShelter) {
            log.warn("Unauthorized chat history access on request id={} by {}", requestId, requesterEmail);
            throw new RuntimeException("Not authorized");
        }
        requireChatAccess(request, requesterEmail);

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
        requireChatAccess(request, senderEmail);

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
