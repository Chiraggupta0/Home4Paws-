package com.home4paws.home4paws.repository;

import com.home4paws.home4paws.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findTopByUserEmailOrderByCreatedAtDesc(String email);

    // Count successful payments (each ACTIVE subscription = one ₹100 payment)
    long countByStatus(String status);

    // Most recent subscriptions first (for admin payments table)
    List<Subscription> findTop20ByStatusOrderByCreatedAtDesc(String status);
}
