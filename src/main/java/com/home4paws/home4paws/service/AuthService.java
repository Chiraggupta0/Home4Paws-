package com.home4paws.home4paws.service;

import com.home4paws.home4paws.dto.AuthResponse;
import com.home4paws.home4paws.model.Role;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Called after Supabase signUp or signIn.
     * Creates the user in our DB on first call; returns their role on subsequent calls.
     * email + metadata come from the validated Supabase JWT.
     */
    public AuthResponse syncUser(String email, Map<String, Object> metadata) {
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);

            // name and role are stored in Supabase user_metadata during signUp
            String name = (String) metadata.getOrDefault("name", "");
            String roleStr = (String) metadata.getOrDefault("role", "NORMAL_USER");

            newUser.setName(name);
            try {
                newUser.setRole(Role.valueOf(roleStr));
            } catch (IllegalArgumentException e) {
                newUser.setRole(Role.NORMAL_USER); // safe default
            }

            return userRepository.save(newUser);
        });

        return new AuthResponse(null, user.getRole().name());
    }
}
