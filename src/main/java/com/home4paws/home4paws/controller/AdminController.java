package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.model.Pet;
import com.home4paws.home4paws.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")   // every endpoint here requires ADMIN
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> users() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> pets() {
        return ResponseEntity.ok(adminService.getAllPets());
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Map<String, Object>>> payments() {
        return ResponseEntity.ok(adminService.getRecentPayments());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        adminService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
