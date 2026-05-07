package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.dto.UserResponse;
import edu.secourse.patientportal.exception.RoleNotFoundException;
import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.service.UserManagementService;
import edu.secourse.patientportal.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller responsible for user management operations.
 *
 * <p>This controller provides endpoints for:
 * <ul>
 *     <li>Retrieving a single user</li>
 *     <li>Retrieving all users</li>
 *     <li>Creating new users</li>
 *     <li>Updating existing users</li>
 *     <li>Deleting users</li>
 * </ul>
 *
 * <p><b>Base Path:</b> {@code /maclogixapi/v1/users}
 *
 * <p><b>Security:</b>
 * <ul>
 *     <li>All endpoints require ADMIN role</li>
 * </ul>
 *
 * <p><b>Design Notes:</b>
 * <ul>
 *     <li>Delegates business logic to {@link UserManagementService}</li>
 *     <li>Uses DTOs (e.g., {@link UserResponse}) for safe API responses</li>
 *     <li>Supports dynamic updates via request body maps</li>
 * </ul>
 */
@RestController
@RequestMapping(Constants.USERS_ENDPOINT)
public class UserController {

    /** Service layer responsible for user operations. */
    private final UserManagementService userService;

    /**
     * Constructs a UserController with required dependencies.
     *
     * @param userService service used for managing users
     */
    public UserController(UserManagementService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a user by username.
     *
     * <p><b>Endpoint:</b> {@code GET /maclogixapi/v1/users/{username}}
     *
     * @param username the username of the user
     * @return 200 OK with user data, or 404 Not Found if user does not exist
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = userService.getUser(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * <p><b>Endpoint:</b> {@code GET /maclogixapi/v1/users}
     *
     * @return 200 OK with list of users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Creates a new user.
     *
     * <p><b>Endpoint:</b> {@code POST /maclogixapi/v1/users/create}
     *
     * <p><b>Expected Request Body:</b>
     * <pre>
     * {
     *   "username": "john123",
     *   "password": "password123",
     *   "name": "John Smith",
     *   "email": "john@mail.com",
     *   "role": "PATIENT" (PATIENT | DOCTOR | ADMIN)
     * }
     * </pre>
     *
     * <p><b>Behavior:</b>
     * <ul>
     *     <li>Creates a user object based on the provided role</li>
     *     <li>Delegates creation to service layer</li>
     *     <li>Returns DTO representation of created user</li>
     * </ul>
     *
     * @param body request body containing user details
     * @return 200 OK with created user, or 400 Bad Request on failure
     * @throws RoleNotFoundException if role is invalid
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            String name = body.get("name");
            String email = body.get("email");
            String role = body.getOrDefault("role", "PATIENT").toUpperCase();

            User user = switch (role) {
                case "DOCTOR" -> new Doctor(username, password, name, email);
                case "ADMIN"  -> new Admin(username, password, name, email);
                case "PATIENT" -> new Patient(username, password, name, email);
                default -> throw new RoleNotFoundException(role + " is not allowed");
            };

            boolean success = userService.createUser(user);

            if (success) {
                return ResponseEntity.ok(UserResponse.fromEntity(user));
            }

            return ResponseEntity.badRequest().body("Could not create user: " + username);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Updates an existing user.
     *
     * <p><b>Endpoint:</b> {@code PUT /maclogixapi/v1/users/{username}}
     *
     * <p><b>Behavior:</b>
     * <ul>
     *     <li>Updates only fields provided in request body</li>
     *     <li>Delegates update logic to service layer</li>
     * </ul>
     *
     * @param username the username of the user to update
     * @param body map containing fields to update
     * @return 200 OK if updated, 400 Bad Request otherwise
     */
    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable String username,
                                        @RequestBody Map<String, String> body) {
        try {
            boolean success = userService.updateUser(username, body);

            if (success) {
                return ResponseEntity.ok("User updated successfully");
            }

            return ResponseEntity.badRequest().body("User not found or username already taken.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Deletes a user by username.
     *
     * <p><b>Endpoint:</b> {@code DELETE /maclogixapi/v1/users/{username}}
     *
     * @param username the username of the user to delete
     * @return 200 OK if deleted, 404 Not Found if user does not exist,
     *         or 500 Internal Server Error if deletion fails
     */
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {

        User user = userService.getUser(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        boolean success = userService.removeUser(user);

        if (success) {
            return ResponseEntity.ok("User deleted: " + username);
        }

        return ResponseEntity.internalServerError().body("Error during deletion.");
    }
}