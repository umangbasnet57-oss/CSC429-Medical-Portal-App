package edu.secourse.patientportal.util;

import edu.secourse.patientportal.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Utility component responsible for JSON Web Token (JWT) operations.
 *
 * <p>This class provides methods for:
 * <ul>
 *     <li>Generating JWT tokens for authenticated users</li>
 *     <li>Validating JWT signatures and expiration status</li>
 *     <li>Extracting usernames and claims from tokens</li>
 *     <li>Checking whether a token has expired</li>
 * </ul>
 *
 * <p><b>Security Behavior:</b>
 * <ul>
 *     <li>Uses HMAC-SHA256 for token signing</li>
 *     <li>Uses a secret key configured in application properties</li>
 *     <li>Uses a configurable expiration time</li>
 * </ul>
 *
 * <p><b>Typical Usage:</b>
 * <ol>
 *     <li>Generate a token after successful login</li>
 *     <li>Send the token to the client</li>
 *     <li>Require the client to include the token in future requests</li>
 *     <li>Validate and parse the token during request filtering</li>
 * </ol>
 */
@Component
public class JwtUtil implements TokenService {

    /** Secret key used to sign and verify JWT tokens. */
    @Value("${jwt.secret.key}")
    private String secretKey;

    /** Token expiration time in milliseconds. */
    @Value("${jwt.expiration.time}")
    private long expirationTime;

    /**
     * Generates a signed JWT token for an authenticated user.
     *
     * <p>The generated token includes:
     * <ul>
     *     <li>The username as the JWT subject</li>
     *     <li>The user role as a custom claim</li>
     *     <li>The issue timestamp</li>
     *     <li>The expiration timestamp</li>
     *     <li>A digital signature to prevent tampering</li>
     * </ul>
     *
     * @param username the authenticated user's username
     * @param role the authenticated user's role
     * @return signed JWT token as a compact string
     */
    @Override
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT token.
     *
     * <p>A token is considered valid when:
     * <ul>
     *     <li>The token has the correct format</li>
     *     <li>The token signature matches the configured secret key</li>
     *     <li>The token has not expired</li>
     * </ul>
     *
     * @param token JWT token to validate
     * @return true if the token is valid; false otherwise
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the username from a JWT token.
     *
     * <p>The username is stored as the JWT subject claim.
     *
     * @param token JWT token
     * @return username stored in the token subject
     */
    @Override
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extracts all claims stored inside a JWT token.
     *
     * <p>Claims may include the subject, role, issue timestamp,
     * expiration timestamp, and any other custom token data.
     *
     * @param token JWT token
     * @return {@link Claims} object containing token payload data
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Determines whether a JWT token has expired.
     *
     * <p>If the token cannot be parsed, it is treated as expired.
     *
     * @param token JWT token to check
     * @return true if the token is expired or invalid; false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Builds the HMAC signing key used for token signing and verification.
     *
     * <p>The configured secret key should be at least 256 bits for HS256.
     *
     * @return {@link SecretKey} used for JWT signing and verification
     */
    private SecretKey getSignatureKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}