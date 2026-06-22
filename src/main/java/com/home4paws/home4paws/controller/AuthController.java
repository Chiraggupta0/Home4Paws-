package com.home4paws.home4paws.controller;

import com.home4paws.home4paws.config.JwtUtil;
import com.home4paws.home4paws.dto.AuthResponse;
import com.home4paws.home4paws.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * POST /api/auth/sync
     * Call this immediately after Supabase signUp OR signIn.
     * Reads the Supabase JWT from the Authorization header,
     * creates/finds the user in our DB using email + user_metadata (name, role),
     * and returns the user's role so the frontend can store it.
     */
    @PostMapping("/sync")
    public ResponseEntity<AuthResponse> syncUser(
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        Map<String, Object> metadata = jwtUtil.extractUserMetadata(token);

        AuthResponse response = authService.syncUser(email, metadata);
        return ResponseEntity.ok(response);
    }
}
