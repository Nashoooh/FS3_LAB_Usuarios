package com.ignacio.fs3_lab_usuarios.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    private static final String SECRET_KEY = "mySecretKeyForJWT2024ThisIsAVeryLongSecretKeyThatMeetsRequirements";
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    public String generateToken(Integer userId, String email, String nombre, String rut, Integer rolId, String rolNombre) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("nombre", nombre);
        claims.put("rut", rut);
        claims.put("rolId", rolId);
        claims.put("rolNombre", rolNombre);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public Integer extractUserId(String token) {
        return extractClaims(token).get("userId", Integer.class);
    }
    
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }
    
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
