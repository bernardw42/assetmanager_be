package com.example.asset_manager.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final byte[] secretBytes;
    private final long expirationMillis;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes:60}") long expirationMinutes
    ) {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMillis = expirationMinutes * 60_000L;
    }

    public String generateToken(CustomUserDetails user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(user.getUsername()) // email
                .claim("employeeId", user.getEmployeeId())
                .claim("role", user.getRole())
                .issuedAt(now)
                .expiration(exp)
                .signWith(Keys.hmacShaKeyFor(secretBytes))
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretBytes))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }
}