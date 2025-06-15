package com.synclyplatform.synclyprojectbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final ObjectMapper objectMapper;

    @Value("${spring.security.jwt.expiration}")
    private long JWT_EXPIRATION_TIME;

    @Value("${spring.security.jwt.secret-key}")
    private String JWT_SECRET;

    public String generateJwtToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return buildJwtToken(claims, userDetails);
    }

    private String buildJwtToken(HashMap<String, Object> claims, UserDetails userDetails) {
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(issuedAt.getTime() + JWT_EXPIRATION_TIME);

        return Jwts.builder()
                .claims(claims)
                .json(new JacksonSerializer<>(objectMapper))
                .subject(userDetails.getUsername())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }

    public boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Date extractExpirationDate(String token) {
        return extractClaims(token).getExpiration();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
