package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.dto.CheckoutRequest;
import com.home4paws.home4paws.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a Razorpay order from the cart
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(
            @RequestBody CheckoutRequest request,
            Principal principal) {
        return ResponseEntity.ok(orderService.checkout(principal.getName(), request.getItems()));
    }

    // Verify payment after checkout
    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestBody Map<String, String> body,
            Principal principal) {
        orderService.verifyPayment(
                principal.getName(),
                body.get("orderId"),
                body.get("paymentId"),
                body.get("signature"));
        return ResponseEntity.ok(Map.of("message", "Order placed"));
    }

    // Order history
    @GetMapping("/my")
    public ResponseEntity<List<Map<String, Object>>> myOrders(Principal principal) {
        return ResponseEntity.ok(orderService.getMyOrders(principal.getName()));
    }
}
