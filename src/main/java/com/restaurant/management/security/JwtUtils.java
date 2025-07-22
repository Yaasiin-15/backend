package com.restaurant.management.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * JWT (JSON Web Token) utility class for the Restaurant Management System.
 * Handles JWT token generation, validation, and extraction of user information.
 * 
 * Features:
 * - JWT token generation with configurable expiration
 * - Token validation with comprehensive error handling
 * - Username extraction from valid tokens
 * - HMAC SHA-256 signature algorithm for security
 * - Base64-encoded secret key support
 * 
 * This class is central to the stateless authentication mechanism.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /** JWT secret key from application properties (Base64 encoded) */
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    /** JWT token expiration time in milliseconds from application properties */
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token for an authenticated user.
     * The token contains the username as subject and has a configurable expiration time.
     * 
     * @param authentication the Spring Security authentication object
     * @return JWT token string
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Creates the signing key from the Base64-encoded secret.
     * Uses HMAC SHA-256 algorithm for token signing.
     * 
     * @return Key object for JWT signing and verification
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts the username from a valid JWT token.
     * The username is stored in the token's subject claim.
     * 
     * @param token the JWT token string
     * @return username extracted from the token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates a JWT token for authenticity and expiration.
     * Performs comprehensive validation including signature verification,
     * expiration check, and format validation.
     * 
     * @param authToken the JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        if (authToken == null || authToken.trim().isEmpty()) {
            logger.warn("JWT token is null or empty");
            return false;
        }
        
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token format: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is not supported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT token validation failed - illegal argument: {} | Token: [{}]", 
                e.getMessage(), authToken != null ? authToken.substring(0, Math.min(20, authToken.length())) + "..." : "null");
        } catch (Exception e) {
            logger.error("Unexpected error during JWT validation: {}", e.getMessage(), e);
        }

        return false;
    }
}