package com.example.demo.configuration;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
	public class JwtUtil {
	    private static final String SECRET_KEY = "YourSecretKeyForJWTValidation"; 
	    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

	    private Key getSigningKey() {
	        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	    }

	    public String generateToken(String username) {
	        return Jwts.builder()
	                .setSubject(username)
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

	    public boolean isTokenValid(String token, String username) {
	        final String extractedUsername = extractClaims(token).getSubject();
	        return (username.equals(extractedUsername) && !isTokenExpired(token));
	    }

	    private boolean isTokenExpired(String token) {
	        return extractClaims(token).getExpiration().before(new Date());
	    }
	}

	


