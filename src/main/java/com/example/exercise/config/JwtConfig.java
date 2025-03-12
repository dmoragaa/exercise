package com.example.exercise.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.exercise.util.JwtUtil;

import java.security.Key;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret:defaultSecretKey}")
    private String secretKey;

    @Bean
    public Key jwtSigningKey() {
        String normalizedKey = normalizeKey(secretKey);
        return Keys.hmacShaKeyFor(normalizedKey.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Normaliza la clave para asegurar que tenga al menos 256 bits (32 bytes)
     */
    private String normalizeKey(String key) {
        StringBuilder normalized = new StringBuilder(key);
        while (normalized.length() < 32) {
            normalized.append(key);
        }
        return normalized.substring(0, 32);
    }

    @Bean
    public JwtUtil jwtUtil(Key jwtSigningKey) {
        return new JwtUtil(jwtSigningKey);
    }
}
