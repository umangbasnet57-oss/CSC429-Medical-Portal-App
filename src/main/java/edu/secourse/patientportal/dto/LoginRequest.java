package edu.secourse.patientportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Login Request DTO for User Authentication
 *
 * Used when a user logs into MacLogix.
 * Validates username and password are provided.
 *
 * HTTP Request Example:
 * POST /maclogixapi/v1/auth/login
 * {
 *   "username": "johndoe",
 *   "password": "SecurePass123"
 * }
 *
 * Success Response (200):
 * {
 *   "success": true,
 *   "message": "Login successful",
 *   "statusCode": 200,
 *   "data": {
 *     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *     "username": "johndoe",
 *     "email": "john@example.com"
 *   }
 * }
 *
 * Error Response (401):
 * {
 *   "success": false,
 *   "message": "Invalid username or password",
 *   "statusCode": 401,
 *   "data": null
 * }
 *
 * @author AquaWorld Development Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request containing username and password")
@Getter
@Setter
public class LoginRequest {

    /**
     * Username for login
     *
     * Validation:
     * - Required (not blank)
     * - Length: 3-20 characters
     */
    @NotBlank(message = "Username is required")
//    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
//    @Schema(description = "Username", example = "johndoe")
    private String username;

    /**
     * Password for login
     *
     * Validation:
     * - Required (not blank)
     * - Length: 8-50 characters (strong password)
     */
    @NotBlank(message = "Password is required")
//    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
//    @Schema(description = "Password", example = "SecurePass123")
    private String password;
}
