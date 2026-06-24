package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final SubscriptionService subscriptionService;

    public PaymentController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // Check subscription status
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus(Principal principal) {
        return ResponseEntity.ok(subscriptionService.getStatus(principal.getName()));
    }

    // Create Razorpay order
    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(Principal principal) {
        return ResponseEntity.ok(subscriptionService.createOrder(principal.getName()));
    }

    // Called by frontend after successful payment
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody Map<String, String> body,
            Principal principal) {
        subscriptionService.activateSubscription(
            principal.getName(),
            body.get("orderId"),
            body.get("paymentId")
        );
        return ResponseEntity.ok(Map.of("message", "Subscription activated"));
    }
}
