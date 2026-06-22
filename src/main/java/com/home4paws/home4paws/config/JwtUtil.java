package com.home4paws.home4paws.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final JwtDecoder jwtDecoder = NimbusJwtDecoder
            .withJwkSetUri("https://fdnngmfcveylwgkwjrov.supabase.co/auth/v1/.well-known/jwks.json")
            .build();

    public String extractEmail(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaimAsString("email");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> extractUserMetadata(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        Object metadata = jwt.getClaim("user_metadata");
        if (metadata instanceof Map) {
            return (Map<String, Object>) metadata;
        }
        return Collections.emptyMap();
    }

    public boolean isTokenValid(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }
}
