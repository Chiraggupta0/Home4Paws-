package com.home4paws.home4paws.service;

import com.home4paws.home4paws.config.JwtUtil;
import com.home4paws.home4paws.dto.AuthResponse;
import com.home4paws.home4paws.dto.LoginRequest;
import com.home4paws.home4paws.dto.RegisterRequest;
import com.home4paws.home4paws.model.User;
import com.home4paws.home4paws.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // UserDetailsService implementation
    // Called by JwtFilter on every request to load user by email

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + email));
    }


    // REGISTER
    // Save new user with hashed password, return JWT token

    public AuthResponse register(RegisterRequest request) {

        // Step 1 — Build user object from request
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // Step 2 — Save user to database
        userRepository.save(user);

        // Step 3 — Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Step 4 — Return token + role in response
        return new AuthResponse(token, user.getRole().name());
    }


    // LOGIN
    // Authenticate user, return JWT token

    public AuthResponse login(LoginRequest request) {

        // Step 1 — Authenticate email + password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Step 2 — Load user from database
        User user = (User) loadUserByUsername(request.getEmail());

        // Step 3 — Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // Step 4 — Return token + role in response
        return new AuthResponse(token, user.getRole().name());
    }
}