package edu.secourse.patientportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Login Response DTO for User Authentication
 *
 * Returned to client after successful login.
 * Contains JWT token and user information.
 *
 * HTTP Response Example:
 * {
 *   "success": true,
 *   "message": "Login successful",
 *   "statusCode": 200,
 *   "data": {
 *     "id": 1001,
 *     "username": "johndoe",
 *     "email": "john@example.com",
 *     "firstName": "John",
 *     "lastName": "Doe",
 *     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *     "tokenType": "Bearer",
 *     "expiresIn": 3600,
 *     "loginAt": "2025-01-18T10:30:00"
 *   }
 * }
 *
 * JWT Token Usage:
 * Add to subsequent requests in Authorization header:
 * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 *
 * @author MacLogix Development Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Login response containing JWT token and user info")
public class LoginResponse {

    /** User ID */
    @Schema(description = "User ID", example = "1001")
    private Integer id;

    /** Username */
    @Schema(description = "Username", example = "johndoe")
    private String username;

    /** User's email address */
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    /** User's first name */
    @Schema(description = "First name", example = "John")
    private String firstName;

    /** User's last name */
    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    /** User's role */
    @Schema(description = "Role", example = "ADMIN")
    private String role;

    /**
     * JWT Access Token
     *
     * Use this token in Authorization header for all authenticated requests:
     * Authorization: Bearer <token>
     *
     * Token contains:
     * - Username (subject)
     * - Issue time
     * - Expiration time
     * - Digital signature
     */
    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    /**
     * Token type (always "Bearer")
     *
     * Used with Authorization header: "Bearer <token>"
     */
    @Schema(description = "Token type", example = "Bearer")
    private String tokenType;

    /**
     * Token expiration time in seconds (3600 = 1 hour)
     *
     * After this time, token becomes invalid and user must login again
     */
    @Schema(description = "Token expiration time in seconds", example = "3600")
    private Long expiresIn;

    /** When the user logged in */
    @Schema(description = "Login timestamp")
    private LocalDateTime loginAt;
}
