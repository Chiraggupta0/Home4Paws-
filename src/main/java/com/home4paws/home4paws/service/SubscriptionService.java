package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.Subscription;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.SubscriptionRepository;
import com.home4paws.home4paws.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepo;
    private final UserRepository userRepo;

    // TODO: Replace with real Razorpay Key ID and Secret from dashboard
    private static final String RAZORPAY_KEY_ID     = "rzp_test_placeholder";
    private static final String RAZORPAY_KEY_SECRET  = "placeholder_secret";
    private static final int    AMOUNT_PAISE         = 10000; // ₹100 in paise

    public SubscriptionService(SubscriptionRepository subscriptionRepo,
                               UserRepository userRepo) {
        this.subscriptionRepo = subscriptionRepo;
        this.userRepo         = userRepo;
    }

    public boolean isSubscribed(String email) {
        Optional<Subscription> sub = subscriptionRepo.findTopByUserEmailOrderByCreatedAtDesc(email);
        if (sub.isEmpty()) return false;
        Subscription s = sub.get();
        return "ACTIVE".equals(s.getStatus()) && s.getEndDate().isAfter(LocalDateTime.now());
    }

    // Creates a mock order (replace with real Razorpay API call when key is ready)
    public Map<String, Object> createOrder(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // TODO: When Razorpay key is ready, call Razorpay Orders API here:
        // RazorpayClient client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);
        // JSONObject options = new JSONObject();
        // options.put("amount", AMOUNT_PAISE);
        // options.put("currency", "INR");
        // options.put("receipt", "rcpt_" + email);
        // Order order = client.orders.create(options);
        // String orderId = order.get("id");

        // For now return a mock order ID
        String mockOrderId = "order_mock_" + System.currentTimeMillis();

        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setStatus("PENDING");
        sub.setRazorpayOrderId(mockOrderId);
        subscriptionRepo.save(sub);

        return Map.of(
            "orderId",  mockOrderId,
            "amount",   AMOUNT_PAISE,
            "currency", "INR",
            "keyId",    RAZORPAY_KEY_ID
        );
    }

    // Called after successful payment from frontend
    public void activateSubscription(String email, String orderId, String paymentId) {
        Subscription sub = subscriptionRepo
                .findTopByUserEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("No pending subscription found"));

        // TODO: Verify Razorpay signature here when real key is ready

        sub.setStatus("ACTIVE");
        sub.setRazorpayPaymentId(paymentId);
        sub.setStartDate(LocalDateTime.now());
        sub.setEndDate(LocalDateTime.now().plusMonths(1));
        subscriptionRepo.save(sub);
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
