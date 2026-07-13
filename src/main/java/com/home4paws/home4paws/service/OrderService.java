package com.home4paws.home4paws.service;

import com.home4paws.home4paws.dto.CheckoutRequest;
import com.home4paws.home4paws.model.Order;
import com.home4paws.home4paws.model.OrderItem;
import com.home4paws.home4paws.model.Product;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.OrderRepository;
import com.home4paws.home4paws.repository.ProductRepository;
import com.home4paws.home4paws.repository.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository   = orderRepository;
        this.productRepository = productRepository;
        this.userRepository    = userRepository;
    }

    // Build an order from the cart + create a Razorpay order
    public Map<String, Object> checkout(String email, List<CheckoutRequest.CartItem> cartItems) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        List<OrderItem> items = new ArrayList<>();
        int total = 0;
        for (CheckoutRequest.CartItem ci : cartItems) {
            Product product = productRepository.findById(ci.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + ci.getProductId()));
            int qty = (ci.getQuantity() != null && ci.getQuantity() > 0) ? ci.getQuantity() : 1;

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(qty);
            oi.setPriceAtPurchase(product.getPrice());

            total += product.getPrice() * qty;
            items.add(oi);
        }
        order.setItems(items);
        order.setTotalAmount(total);

        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            JSONObject options = new JSONObject();
            options.put("amount", total * 100); // paise
            options.put("currency", "INR");
            options.put("receipt", "order_" + user.getId() + "_" + System.currentTimeMillis());

            com.razorpay.Order rzpOrder = client.orders.create(options);
            String rzpOrderId = rzpOrder.get("id");
            order.setRazorpayOrderId(rzpOrderId);
            orderRepository.save(order);

            return Map.of(
                "orderId",  rzpOrderId,
                "amount",   total * 100,
                "currency", "INR",
                "keyId",    razorpayKeyId,
                "total",    total
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }

    // Verify the Razorpay signature and mark the order paid
    public void verifyPayment(String email, String orderId, String paymentId, String signature) {
        Order order = orderRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);

            boolean valid = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
            if (!valid) throw new RuntimeException("Invalid payment signature");
        } catch (Exception e) {
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }

        order.setStatus("PAID");
        order.setRazorpayPaymentId(paymentId);
        orderRepository.save(order);
    }

    // Order history as plain maps (avoids serialising entities / lazy relations)
    public List<Map<String, Object>> getMyOrders(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email).stream().map(o -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", o.getId());
            m.put("total", o.getTotalAmount());
            m.put("status", o.getStatus());
            m.put("paymentId", o.getRazorpayPaymentId());
            m.put("createdAt", o.getCreatedAt() != null ? o.getCreatedAt().toString() : "");
            m.put("items", o.getItems().stream().map(it -> {
                Map<String, Object> im = new HashMap<>();
                im.put("name", it.getProduct().getName());
                im.put("imageUrl", it.getProduct().getImageUrl());
                im.put("quantity", it.getQuantity());
                im.put("price", it.getPriceAtPurchase());
                return im;
            }).toList());
            return m;
        }).toList();
    }
}
