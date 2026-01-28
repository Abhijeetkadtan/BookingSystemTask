package com.example.booking.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String SECRET = "abhijeet_very_secret_key_123456_abhijeet_very_secret_key_123456";

    public JwtUtil() {
    }

    public String generateToken(String username, String role) {
        return Jwts.builder().setSubject(username).claim("role", role).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 86400000L)).signWith(Keys.hmacShaKeyFor("abhijeet_very_secret_key_123456_abhijeet_very_secret_key_123456".getBytes()), SignatureAlgorithm.HS256).compact();
    }

    public Claims extractClaims(String token) {
        return (Claims)Jwts.parserBuilder().setSigningKey("abhijeet_very_secret_key_123456_abhijeet_very_secret_key_123456".getBytes()).build().parseClaimsJws(token).getBody();
    }
}
