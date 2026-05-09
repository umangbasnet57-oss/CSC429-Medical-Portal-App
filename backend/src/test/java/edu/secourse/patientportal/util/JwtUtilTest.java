package edu.secourse.patientportal.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(
                jwtUtil,
                "secretKey",
                "thisIsASecretKeyThatIsAtLeast32BytesLong"
        );

        ReflectionTestUtils.setField(
                jwtUtil,
                "expirationTime",
                3600000L
        );
    }

    @Test
    void generateTokenShouldCreateValidToken() {
        String token = jwtUtil.generateToken("john", "PATIENT");

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void extractUsernameShouldReturnUsername() {
        String token = jwtUtil.generateToken("john", "PATIENT");

        String username = jwtUtil.extractUsername(token);

        assertEquals("john", username);
    }

    @Test
    void extractAllClaimsShouldReturnClaims() {
        String token = jwtUtil.generateToken("john", "ADMIN");

        Claims claims = jwtUtil.extractAllClaims(token);

        assertEquals("john", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void validateTokenShouldReturnFalseForInvalidToken() {
        boolean result = jwtUtil.validateToken("invalid.token.value");

        assertFalse(result);
    }

    @Test
    void isTokenExpiredShouldReturnFalseForValidToken() {
        String token = jwtUtil.generateToken("john", "DOCTOR");

        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void isTokenExpiredShouldReturnTrueForInvalidToken() {
        assertTrue(jwtUtil.isTokenExpired("bad-token"));
    }

    @Test
    void expiredTokenShouldBeExpired() {
        JwtUtil expiredJwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(
                expiredJwtUtil,
                "secretKey",
                "thisIsASecretKeyThatIsAtLeast32BytesLong"
        );

        ReflectionTestUtils.setField(
                expiredJwtUtil,
                "expirationTime",
                -1000L
        );

        String token = expiredJwtUtil.generateToken("john", "PATIENT");

        assertTrue(expiredJwtUtil.isTokenExpired(token));
    }
}