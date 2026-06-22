package com.home4paws.home4paws.config;

import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final JwtDecoder jwtDecoder = NimbusJwtDecoder
            .withJwkSetUri("https://fdnngmfcveylwgkwjrov.supabase.co/auth/v1/.well-known/jwks.json")
            .jwsAlgorithm(SignatureAlgorithm.ES256)
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
            // Log the kid from the token header for debugging
            SignedJWT signed = SignedJWT.parse(token);
            String kid = signed.getHeader().getKeyID();
            String alg = signed.getHeader().getAlgorithm().getName();
            log.info("Token kid={} alg={}", kid, alg);
            jwtDecoder.decode(token);
            return true;
        } catch (ParseException e) {
            log.error("JWT parse failed: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }
}
