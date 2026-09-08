package com.home4paws.home4paws.service;

import com.home4paws.home4paws.dto.AuthResponse;
import com.home4paws.home4paws.model.Role;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Called after Supabase signUp or signIn.
     * Creates the user in our DB on first call; returns their role on subsequent calls.
     * email + metadata come from the validated Supabase JWT.
     *
     * requestedRole: role picked on the register page, passed explicitly by the
     * frontend for Google OAuth signups (Supabase doesn't carry our custom
     * user_metadata through the OAuth redirect the way it does for email signUp).
     * Only used when creating a brand-new user; ignored for existing users.
     */
    public AuthResponse syncUser(String email, Map<String, Object> metadata, String requestedRole) {
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);

            // name and role are stored in Supabase user_metadata during email/password signUp;
            // for Google OAuth signups, requestedRole carries the role instead.
            String name = (String) metadata.getOrDefault("name", "");
            String roleStr = requestedRole != null ? requestedRole
                    : (String) metadata.getOrDefault("role", "NORMAL_USER");

            newUser.setName(name);
            try {
                newUser.setRole(Role.valueOf(roleStr));
            } catch (IllegalArgumentException e) {
                log.warn("Unknown role '{}' for new user {}, defaulting to NORMAL_USER", roleStr, email);
                newUser.setRole(Role.NORMAL_USER); // safe default
            }

            User saved = userRepository.save(newUser);
            log.info("Registered new user email={} role={}", email, saved.getRole());
            return saved;
        });

        log.info("User synced email={} role={}", email, user.getRole());
        return new AuthResponse(null, user.getRole().name());
    }
}
