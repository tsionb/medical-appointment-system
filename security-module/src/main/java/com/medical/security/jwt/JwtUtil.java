package com.medical.security.jwt;

import com.medical.common.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {


    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;


    public String generateToken(String email, Role role, Long profileId) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());

        if (profileId != null) {
            claims.put("profileId", profileId);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }


    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }


    public Long extractProfileId(String token) {
        Object profileId = extractAllClaims(token).get("profileId");
        if (profileId == null) return null;

        return ((Number) profileId).longValue();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("JWT expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT unsupported: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("JWT malformed: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("JWT signature invalid: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims empty: " + e.getMessage());
        }
        return false;
    }


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

 
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}