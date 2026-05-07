package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.exception.UnauthorizedException;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import edu.secourse.patientportal.util.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service responsible for handling authentication and JWT-related operations.
 *
 * <p>This service provides functionality for:
 * <ul>
 *     <li>User authentication (login)</li>
 *     <li>JWT token generation</li>
 *     <li>JWT token validation</li>
 *     <li>Extracting user identity from tokens</li>
 * </ul>
 *
 * <p><b>Authentication Flow:</b>
 * <ol>
 *     <li>Retrieve user by username</li>
 *     <li>Validate password using {@link PasswordEncoder}</li>
 *     <li>Update user's last login timestamp</li>
 *     <li>Generate JWT token via {@link TokenService}</li>
 *     <li>Return {@link LoginResponse} containing token and user details</li>
 * </ol>
 *
 * <p><b>Security Notes:</b>
 * <ul>
 *     <li>Passwords are securely stored using hashing (e.g., BCrypt)</li>
 *     <li>JWT tokens are stateless and time-limited</li>
 *     <li>Invalid credentials result in {@link UnauthorizedException}</li>
 * </ul>
 */
@Service
public class AuthenticationService implements AuthenticationManagementService {

    /** Repository used to retrieve and persist user data. */
    private final UserRepository userRepository;

    /** Encoder used to verify hashed passwords. */
    private final PasswordEncoder passwordEncoder;

    /** Service responsible for JWT token operations. */
    private final TokenService tokenService;

    /**
     * Constructs an AuthenticationService with required dependencies.
     *
     * @param userRepository repository for user data access
     * @param passwordEncoder encoder for password verification
     * @param tokenService service for JWT operations
     */
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * <p>This method performs the following:
     * <ul>
     *     <li>Validates that the user exists</li>
     *     <li>Compares raw password with stored hashed password</li>
     *     <li>Updates last login timestamp</li>
     *     <li>Generates a JWT token</li>
     * </ul>
     *
     * @param loginRequest contains username and password
     * @return {@link LoginResponse} containing user info and JWT token
     * @throws UnauthorizedException if credentials are invalid
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        // Retrieve user or throw 401 Unauthorized
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UnauthorizedException(Constants.MSG_INVALID_CREDENTIALS));

        // Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(Constants.MSG_INVALID_CREDENTIALS);
        }

        // Normalize role
        user.setRole(user.getRole().toUpperCase());

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT token
        String token = tokenService.generateToken(user.getUsername(), user.getRole());

        // Build and return response
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour (seconds)
                .loginAt(LocalDateTime.now())
                .build();
    }

    /**
     * Validates a JWT token.
     *
     * <p>This method checks whether a token is:
     * <ul>
     *     <li>Properly signed</li>
     *     <li>Not expired</li>
     * </ul>
     *
     * @param token the JWT token
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        return tokenService.validateToken(token);
    }

    /**
     * Extracts the username from a JWT token.
     *
     * <p>This is typically used to identify the currently authenticated user
     * during request processing.
     *
     * @param token the JWT token
     * @return username embedded in the token
     */
    public String getUsernameFromToken(String token) {
        return tokenService.extractUsername(token);
    }
}