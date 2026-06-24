package com.home4paws.home4paws.repository;

import com.home4paws.home4paws.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findTopByUserEmailOrderByCreatedAtDesc(String email);
}
