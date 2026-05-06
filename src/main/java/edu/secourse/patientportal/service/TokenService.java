package edu.secourse.patientportal.service;

public interface TokenService {
    String generateToken(String username, String role);
    boolean validateToken(String token);
    String extractUsername(String token);
}
