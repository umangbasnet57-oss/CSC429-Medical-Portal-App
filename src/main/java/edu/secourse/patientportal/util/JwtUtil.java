package edu.secourse.patientportal.util;

import edu.secourse.patientportal.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * JWT (JSON Web Token) Utility Class for AquaWorld REST API
 *
 * This utility class handles:
 * - JWT token generation for authenticated users
 * - JWT token validation and verification
 * - Extracting user information from tokens
 * - Token expiration checking
 *
 * JWT Structure: header.payload.signature
 *
 * Security:
 * - Uses HMAC-SHA256 algorithm for signing
 * - Token expiration: 1 hour (configurable)
 * - Secret key: Must be at least 256 bits long (32 characters)
 *
 * Usage:
 * 1. After successful login: generateToken(username) -> return to client
 * 2. On each request: validateToken(token) -> verify authenticity
 * 3. Extract username: extractUsername(token) -> identify user
 *
 * @author AquaWorld Development Team
 */
@Component
public class JwtUtil implements TokenService {

    // Injected from application.properties
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private long expirationTime; // in milliseconds

    /**
     * Generates a JWT token for a user after successful authentication
     *
     * The token contains:
     * - Username (subject)
     * - Issue time (iat)
     * - Expiration time (exp)
     * - Digital signature (to prevent tampering)
     *
     * @param username the username to encode in the token
     * @return JWT token as a string
     */
    @Override
    public String generateToken(String username, String role) {
        return Jwts.builder()
                // Set the subject (username)
                .setSubject(username)
                // Claim user role
                .claim("role", role)
//                .claim("roles", List.of("ROLE_USER"))
                // Set issue time (current date/time)
                .setIssuedAt(new Date())
                // Set expiration time
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                // Sign the token with the secret key using HMAC-SHA256
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                // Compact the token into a string
                .compact();
    }

    /**
     * Validates JWT token authenticity and checks expiration
     *
     * The token is valid if:
     * - Signature is correct (not tampered with)
     * - Token has not expired
     * - Token format is correct
     *
     * @param token the JWT token to validate
     * @return true if token is valid, false otherwise
     */
    @Override
    public boolean validateToken(String token) {
        try {
            // Parse and verify the token signature
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Log token validation failure (optional)
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts the username from a valid JWT token
     *
     * The username is stored as the "subject" claim in the JWT payload
     *
     * @param token the JWT token
     * @return username extracted from the token
     */
    @Override
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts all claims (information) from the JWT token
     *
     * Claims include: subject, issue time, expiration, etc.
     *
     * @param token the JWT token
     * @return Claims object containing all token information
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the token has expired
     *
     * @param token the JWT token
     * @return true if token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // Consider expired if any error
        }
    }

    /**
     * Gets the signature key for token signing and verification
     *
     * Converts the secret key string to a SecretKey object
     * The key must be at least 256 bits for HMAC-SHA256
     *
     * @return SecretKey for HMAC-SHA256 signing
     */
    private SecretKey getSignatureKey() {
        // Ensure the key is at least 256 bits (32 bytes)
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
