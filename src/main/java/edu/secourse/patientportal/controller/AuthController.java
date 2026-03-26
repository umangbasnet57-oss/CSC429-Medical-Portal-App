package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.dto.RegisterRequest;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.response.MLogixApiResponse;
import edu.secourse.patientportal.service.AuthenticationService;
import edu.secourse.patientportal.service.UserService;
import edu.secourse.patientportal.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Authentication REST Controller for Maclogix
 *
 * Provides endpoints for user authentication:
 * - User registration (create new account) by Administrator
 * - User login (authenticate and get JWT token)
 *
 * Base Path: /maclogixapi/v1/auth
 * All endpoints are PUBLIC (no JWT required)
 *
 * HTTP Response Status Codes:
 * - 200 OK: Login successful
 * - 201 Created: Registration successful
 * - 400 Bad Request: Invalid input (validation error)
 * - 401 Unauthorized: Invalid credentials (login failed)
 * - 409 Conflict: Username or email already exists
 * - 500 Internal Server Error: Server error
 *
 * Security Notes:
 * - Passwords are encrypted using BCrypt (never stored in plain text)
 * - JWT tokens are issued on successful login
 * - JWT tokens expire after 1 hour
 *
 * @author MacLogix Development Team
 */

@RestController
@RequestMapping(Constants.AUTH_ENDPOINT)
@Tag(name = "Authentication", description = "User Creation and Login")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(UserService userService, AuthenticationService authenticationService){
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * Register a new user account
     *
     * Creates a new MacLogix user account.
     *
     * HTTP Request:
     * POST /maclogixapi/v1/auth/register
     * Content-Type: application/json
     * {
     *   "username": "johndoe",
     *   "email": "john@example.com",
     *   "password": "SafePwd123",
     *   "firstName": "John",
     *   "lastName": "Doe",
     * }
     *
     * Success Response (201 Created):
     * {
     *   "success": true,
     *   "message": "Registration successful",
     *   "statusCode": 201,
     *   "data": {
     *     "id": 1,
     *     "username": "johndoe",
     *     "email": "john@example.com",
     *     "firstName": "John",
     *     "lastName": "Doe"
     *   }
     * }
     *
     * Error Responses:
     * - 400 Bad Request: Missing/invalid fields
     * - 409 Conflict: Username or email already exists
     *
     * @param registerRequest contains username, email, password, firstName, lastName
     * @return ResponseEntity with success message and user data (201 Created)
     */

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new MacLogix user account")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
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
        // Call service to register (create) new user
        User registerdUser = userService.register(registerRequest);

        // Successful response
        MLogixApiResponse response = MLogixApiResponse.success(
                Constants.MSG_REGISTRATION_SUCCESS,
                registerdUser,
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * User login endpoint
     *
     * Authenticates user and returns JWT token.
     *
     * HTTP Request:
     * POST /api/v1/auth/login
     * Content-Type: application/json
     * {
     *   "username": "johndoe",
     *   "password": "SecurePass123"
     * }
     *
     * Success Response (200 OK):
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
     * Error Response (401 Unauthorized):
     * {
     *   "success": false,
     *   "message": "Invalid username or password",
     *   "statusCode": 401,
     *   "data": null
     * }
     *
     * JWT Token Usage:
     * Add token to all subsequent requests in Authorization header:
     * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     *
     * @param loginRequest contains username and password
     * @return ResponseEntity with JWT token and user info (200 OK)
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and get JWT token")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful, JWT token returned",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
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
        // Call service to authenticate and get JWT token
        LoginResponse loginResponse = authenticationService.login(loginRequest);

        System.out.println("Trying to track user role: " +loginResponse.getRole());

        // Return success response (200 OK)
        MLogixApiResponse<LoginResponse> response = MLogixApiResponse.success(
                Constants.MSG_LOGIN_SUCCESS,
                loginResponse,
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
