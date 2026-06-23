package com.home4paws.home4paws.repository;

import com.home4paws.home4paws.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByAdoptionRequestIdOrderBySentAtAsc(Long requestId);
}
