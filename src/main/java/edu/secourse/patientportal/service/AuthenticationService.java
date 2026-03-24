package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.exception.UnauthorizedException;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import edu.secourse.patientportal.util.Constants;
import edu.secourse.patientportal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

//    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticate user and generate JWT token
     *
     * Process:
     * 1. Find user by username
     * 2. If not found -> throw 401 Unauthorized
     * 3. Compare provided password with stored encrypted password
     * 4. If password doesn't match -> throw 401 Unauthorized
     * 5. Update last login time
     * 6. Generate JWT token
     * 7. Return login response with token
     *
     * @param loginRequest contains username and password
     * @return LoginResponse with JWT token and user info
     * @throws UnauthorizedException if username not found or password invalid (401)
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // Find user by username
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UnauthorizedException(Constants.MSG_INVALID_CREDENTIALS));

        System.out.println("User details: "+ user.toString());
        // Verify password
        // passwordEncoder.matches() compares plain text with BCrypt encrypted password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(Constants.MSG_INVALID_CREDENTIALS);
        }

        // Make sure role is capitalized
        user.setRole(user.getRole().toUpperCase());

        // Update last login timestamp
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        System.out.println("This is the generatied token:\n"+ token);
        System.out.println(("This is the username and role: " + user.getUsername() + " " + user.getRole()));

        // Return login response with token
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour in seconds
                .loginAt(LocalDateTime.now())
                .build();
    }

    /**
     * Validate JWT token
     *
     * Used to check if a token is still valid
     *
     * @param token the JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Extract username from JWT token
     *
     * Used to identify the authenticated user
     *
     * @param token the JWT token
     * @return username from token
     */
    public String getUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }
}
