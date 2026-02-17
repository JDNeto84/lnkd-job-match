package com.matchjob.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.matchjob.application.port.outgoing.TokenService;

import javax.crypto.SecretKey;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenServiceImpl implements TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-minutes}")
    private long expirationMinutes;

    public long getExpirationMinutes() {
        return expirationMinutes;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Segredo JWT deve estar codificado em Base64");
        }
        if (keyBytes.length < 32) {
            throw new IllegalStateException("Segredo JWT deve ter pelo menos 256 bits (32 bytes) em Base64");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @PostConstruct
    public void validateSecret() {
        getSigningKey();
    }

    @Override
    public String generateToken(String subject, List<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, String subject) {
        String tokenSubject = getSubject(token);
        return tokenSubject.equals(subject) && !isTokenExpired(token);
    }

    @Override
    public String getSubject(String token) {
        return getAllClaims(token).getSubject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Claims claims = getAllClaims(token);
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?>) {
            return (List<String>) rolesObj;
        }
        return List.of();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
