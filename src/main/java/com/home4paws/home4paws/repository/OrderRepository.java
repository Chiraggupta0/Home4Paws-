package com.home4paws.home4paws.repository;

import com.home4paws.home4paws.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);

    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);
}
