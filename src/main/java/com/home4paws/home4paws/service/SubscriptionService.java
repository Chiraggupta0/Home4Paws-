package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.Subscription;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.SubscriptionRepository;
import com.home4paws.home4paws.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final UserRepository userRepo;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private static final int AMOUNT_PAISE = 10000; // ₹100 in paise

    public SubscriptionService(SubscriptionRepository subscriptionRepo,
                               UserRepository userRepo) {
        this.subscriptionRepo = subscriptionRepo;
        this.userRepo         = userRepo;
    }

    public boolean isSubscribed(String email) {
        Optional<Subscription> sub = subscriptionRepo.findTopByUserEmailOrderByCreatedAtDesc(email);
        if (sub.isEmpty()) return false;
        Subscription s = sub.get();
        return "ACTIVE".equals(s.getStatus())
                && s.getEndDate() != null
                && s.getEndDate().isAfter(LocalDateTime.now());
    }

    // Creates a real Razorpay order
    public Map<String, Object> createOrder(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject options = new JSONObject();
            options.put("amount", AMOUNT_PAISE);
            options.put("currency", "INR");
            options.put("receipt", "rcpt_" + user.getId() + "_" + System.currentTimeMillis());

            Order order = client.orders.create(options);
            String orderId = order.get("id");

            Subscription sub = new Subscription();
            sub.setUser(user);
            sub.setStatus("PENDING");
            sub.setRazorpayOrderId(orderId);
            subscriptionRepo.save(sub);

            log.info("Razorpay order created orderId={} user={} amount={}", orderId, email, AMOUNT_PAISE);
            return Map.of(
                "orderId",  orderId,
                "amount",   AMOUNT_PAISE,
                "currency", "INR",
                "keyId",    razorpayKeyId
            );
        } catch (Exception e) {
            log.error("Failed to create Razorpay order for user={}", email, e);
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    // Verifies Razorpay signature and activates subscription
    public void activateSubscription(String email, String orderId, String paymentId, String signature) {
        Subscription sub = subscriptionRepo
                .findTopByUserEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("No pending subscription found"));

        // Verify the payment signature came from Razorpay
        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);

            boolean valid = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
            if (!valid) throw new RuntimeException("Invalid payment signature");
        } catch (Exception e) {
            log.warn("Payment verification failed user={} orderId={}: {}", email, orderId, e.getMessage());
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }

        sub.setStatus("ACTIVE");
        sub.setRazorpayPaymentId(paymentId);
        sub.setStartDate(LocalDateTime.now());
        sub.setEndDate(LocalDateTime.now().plusMonths(1)); // 30-day validity
        subscriptionRepo.save(sub);
        log.info("Subscription activated user={} orderId={} paymentId={}", email, orderId, paymentId);
    }

    public Map<String, Object> getStatus(String email) {
        boolean subscribed = isSubscribed(email);
        Optional<Subscription> sub = subscriptionRepo.findTopByUserEmailOrderByCreatedAtDesc(email);
        LocalDateTime endDate = sub.filter(s -> "ACTIVE".equals(s.getStatus()))
                                   .map(Subscription::getEndDate)
                                   .orElse(null);
        return Map.of(
            "subscribed", subscribed,
            "endDate",    endDate != null ? endDate.toString() : ""
        );
    }
}
