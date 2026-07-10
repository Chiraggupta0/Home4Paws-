package com.home4paws.home4paws.service;

import com.home4paws.home4paws.model.*;
import com.home4paws.home4paws.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdminService {

    private final UserRepository userRepo;
    private final PetRepository petRepo;
    private final AdoptionRequestRepository requestRepo;
    private final SubscriptionRepository subscriptionRepo;

    private static final int PRICE_PER_SUB = 100; // ₹100 per subscription

    public AdminService(UserRepository userRepo,
                        PetRepository petRepo,
                        AdoptionRequestRepository requestRepo,
                        SubscriptionRepository subscriptionRepo) {
        this.userRepo         = userRepo;
        this.petRepo          = petRepo;
        this.requestRepo      = requestRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    // Overview stats for the dashboard
    public Map<String, Object> getStats() {
        List<User> users = userRepo.findAll();

        long totalUsers   = users.size();
        long adopters     = users.stream().filter(u -> u.getRole() == Role.NORMAL_USER).count();
        long sellers      = users.stream().filter(u -> u.getRole() == Role.SELLER).count();
        long ngos         = users.stream().filter(u -> u.getRole() == Role.NGO_SHELTER).count();

        long totalPets    = petRepo.count();
        long availablePets= petRepo.findByStatus(PetStatus.AVAILABLE).size();
        long adoptedPets  = petRepo.findByStatus(PetStatus.ADOPTED).size();

        long totalRequests   = requestRepo.count();
        long pendingRequests = requestRepo.findByStatus(RequestStatus.PENDING).size();
        long approved        = requestRepo.findByStatus(RequestStatus.APPROVED).size();

        long activeSubs   = subscriptionRepo.countByStatus("ACTIVE");
        long totalEarning = activeSubs * PRICE_PER_SUB;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("adopters", adopters);
        stats.put("sellers", sellers);
        stats.put("ngos", ngos);
        stats.put("totalPets", totalPets);
        stats.put("availablePets", availablePets);
        stats.put("adoptedPets", adoptedPets);
        stats.put("totalRequests", totalRequests);
        stats.put("pendingRequests", pendingRequests);
        stats.put("approvedRequests", approved);
        stats.put("activeSubscriptions", activeSubs);
        stats.put("totalEarnings", totalEarning);
        return stats;
    }

    // All users (for management table)
    public List<Map<String, Object>> getAllUsers() {
        return userRepo.findAll().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId());
            m.put("name", u.getName());
            m.put("email", u.getEmail());
            m.put("role", u.getRole() != null ? u.getRole().name() : "");
            m.put("phoneNumber", u.getPhoneNumber());
            m.put("createdAt", u.getCreatedAt() != null ? u.getCreatedAt().toString() : "");
            return m;
        }).toList();
    }

    // All pets
    public List<Pet> getAllPets() {
        return petRepo.findAll();
    }

    // Recent payments
    public List<Map<String, Object>> getRecentPayments() {
        return subscriptionRepo.findTop20ByStatusOrderByCreatedAtDesc("ACTIVE").stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("user", s.getUser() != null ? s.getUser().getEmail() : "");
            m.put("amount", PRICE_PER_SUB);
            m.put("paymentId", s.getRazorpayPaymentId());
            m.put("startDate", s.getStartDate() != null ? s.getStartDate().toString() : "");
            m.put("endDate", s.getEndDate() != null ? s.getEndDate().toString() : "");
            return m;
        }).toList();
    }

    // Admin can delete any user
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
        log.info("Admin deleted user id={}", id);
    }

    // Admin can delete any pet
    public void deletePet(Long id) {
        petRepo.deleteById(id);
        log.info("Admin deleted pet id={}", id);
    }
}
