package edu.secourse.patientportal.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Registration request for new user account")

public class RegisterRequest {
    /**
     * Username for the new account
     *
     * Validation:
     * - Required (not blank)
     * - Length: 3-20 characters
     * - Must be unique in the system
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Schema(description = "Unique username", example = "johndoe")
    private String username;

    /**
     * Email address for the new account
     *
     * Validation:
     * - Required (not blank)
     * - Must be valid email format
     * - Must be unique in the system
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Unique email address", example = "john@example.com")
    private String email;

    /**
     * Password for the new account
     *
     * Validation:
     * - Required (not blank)
     * - Length: 8-50 characters (strong password)
     * - Will be encrypted using BCrypt before storage
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Schema(description = "Password (will be encrypted)", example = "SafePwd123")
    private String password;

    /**
     * User's first name
     *
     * Validation:
     * - Required (not blank)
     * - Max length: 50 characters
     */
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Schema(description = "First name", example = "John")
    private String firstName;

    /**
     * User's last name
     *
     * Validation:
     * - Required (not blank)
     * - Max length: 50 characters
     */
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    /**
     * User's role
     *
     * Validation:
     * - Required (not blank)
     */
    @NotBlank
    private String role;
}
