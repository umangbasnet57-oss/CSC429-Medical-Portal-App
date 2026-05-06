package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.dto.RegisterRequest;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.response.MLogixApiResponse;
import edu.secourse.patientportal.service.AuthenticationManagementService;
import edu.secourse.patientportal.service.UserManagementService;
import edu.secourse.patientportal.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST Controller responsible for authentication and user registration operations.
 *
 * <p>This controller exposes endpoints for:
 * <ul>
 *     <li>User registration (creation of new accounts)</li>
 *     <li>User authentication (login and JWT token issuance)</li>
 * </ul>
 *
 * <p><b>Base Path:</b> {@code /maclogixapi/v1/auth}
 *
 * <p><b>Security Behavior:</b>
 * <ul>
 *     <li>Registration endpoint requires ADMIN role</li>
 *     <li>Login endpoint is publicly accessible</li>
 * </ul>
 *
 * <p><b>Authentication Flow:</b>
 * <ol>
 *     <li>User submits credentials via login endpoint</li>
 *     <li>AuthenticationService validates credentials</li>
 *     <li>JWT token is generated and returned</li>
 *     <li>Client uses token for subsequent authenticated requests</li>
 * </ol>
 *
 * <p><b>Security Notes:</b>
 * <ul>
 *     <li>Passwords are encrypted using BCrypt</li>
 *     <li>JWT tokens are time-limited (default: 1 hour)</li>
 *     <li>Tokens must be included in the Authorization header</li>
 * </ul>
 *
 * <p><b>Common HTTP Responses:</b>
 * <ul>
 *     <li>200 OK – Successful login</li>
 *     <li>201 Created – Successful registration</li>
 *     <li>400 Bad Request – Validation or malformed request</li>
 *     <li>401 Unauthorized – Invalid credentials</li>
 *     <li>409 Conflict – Duplicate username/email</li>
 *     <li>500 Internal Server Error – Unexpected failure</li>
 * </ul>
 *
 * @author MacLogix Development Team
 */
@RestController
@RequestMapping(Constants.AUTH_ENDPOINT)
@Tag(name = "Authentication", description = "User Creation and Login")
public class AuthController {

    private final UserManagementService userService;
    private final AuthenticationManagementService authenticationService;

    @Autowired
    public AuthController(UserManagementService userService, AuthenticationManagementService authenticationService){
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * Registers a new user account.
     *
     * <p>This endpoint allows an administrator to create a new user in the system.
     * The request body must contain all required user fields, which are validated
     * before processing.
     *
     * <p><b>Endpoint:</b> {@code POST /maclogixapi/v1/auth/register}
     *
     * <p><b>Request Body:</b>
     * <pre>
     * {
     *   "username": "johndoe",
     *   "email": "john@example.com",
     *   "password": "SafePwd123",
     *   "firstName": "John",
     *   "lastName": "Doe"
     * }
     * </pre>
     *
     * <p><b>Success Response (201 Created):</b>
     * Returns the created user wrapped in {@link MLogixApiResponse}.
     *
     * <p><b>Error Cases:</b>
     * <ul>
     *     <li>400 Bad Request – Validation failure</li>
     *     <li>409 Conflict – Username or email already exists</li>
     * </ul>
     *
     * @param registerRequest DTO containing user registration details
     * @return ResponseEntity containing the created user and success message
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new MacLogix user account")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = MLogixApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input - validation error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Username or email already exists"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MLogixApiResponse<User>> register(@Valid @RequestBody RegisterRequest registerRequest){

        User registeredUser = userService.register(registerRequest);

        MLogixApiResponse<User> response = MLogixApiResponse.success(
                Constants.MSG_REGISTRATION_SUCCESS,
                registeredUser,
                HttpStatus.CREATED.value()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * <p>This endpoint validates the provided credentials and returns a signed
     * JWT token upon successful authentication. The token can then be used
     * to access protected endpoints.
     *
     * <p><b>Endpoint:</b> {@code POST /maclogixapi/v1/auth/login}
     *
     * <p><b>Request Body:</b>
     * <pre>
     * {
     *   "username": "johndoe",
     *   "password": "SecurePass123"
     * }
     * </pre>
     *
     * <p><b>Success Response (200 OK):</b>
     * Returns a {@link LoginResponse} containing user details and JWT token.
     *
     * <p><b>Error Cases:</b>
     * <ul>
     *     <li>400 Bad Request – Invalid request format</li>
     *     <li>401 Unauthorized – Invalid username or password</li>
     * </ul>
     *
     * <p><b>JWT Usage:</b>
     * Include the token in the Authorization header:
     * <pre>
     * Authorization: Bearer &lt;token&gt;
     * </pre>
     *
     * @param loginRequest DTO containing username and password
     * @return ResponseEntity containing authentication result and JWT token
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and get JWT token")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful, JWT token returned",
                    content = @Content(schema = @Schema(implementation = MLogixApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password"
            )
    })
    public ResponseEntity<MLogixApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = authenticationService.login(loginRequest);

        MLogixApiResponse<LoginResponse> response = MLogixApiResponse.success(
                Constants.MSG_LOGIN_SUCCESS,
                loginResponse,
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}