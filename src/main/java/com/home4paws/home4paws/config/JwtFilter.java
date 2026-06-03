package com.home4paws.home4paws.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Constructor injection — Spring injects both automatically
    public JwtFilter(JwtUtil jwtUtil,@Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1 — Read the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2 — Check if header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token — pass request along without setting authentication
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3 — Extract the token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // Step 4 — Extract email from token
        String email = jwtUtil.extractEmail(token);

        // Step 5 — If email found and no authentication set yet
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6 — Load user from database using email
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Step 7 — Validate the token
            if (jwtUtil.isTokenValid(token, userDetails.getUsername())) {

                // Step 8 — Create authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Step 9 — Attach request details
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Step 10 — Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 11 — Continue to next filter or controller
        filterChain.doFilter(request, response);
    }
}