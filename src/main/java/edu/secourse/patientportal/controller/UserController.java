package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.exception.RoleNotFoundException;
import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.service.UserService;
import edu.secourse.patientportal.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for user management.
 * Base URL: /maclogixapi/v1/users
 */
@RestController
@RequestMapping(Constants.USERS_ENDPOINT)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Debug controll
    @GetMapping("/debug")
    public String debug(Authentication authentication) {
        System.out.println("Trying to display User role...");
        authentication.getAuthorities()
                .forEach(a -> System.out.println(a.getAuthority()));

        return "check console";
    }
//    @GetMapping("/debug")
//    @PreAuthorize("permitAll()")
//    public List<String> debug(Authentication authentication) {
//        if(authentication == null) return List.of("No authentication");
//        return authentication.getAuthorities()
//                .stream()
//                .map(a -> a.getAuthority())
//                .toList();
//    }

    /**
     * GET /maclogixapi/v1/users/{username}
     * Returns a user's information by their username.
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * POST /api/users
     * Create a new User.
     * Body JSON :
     * {
     *   "username": "john123",
     *   "password": "pass123",
     *   "name":     "John Smith",
     *   "email":    "john@mail.com",
     *   "role":     "patient"  (patient | doctor | admin)
     * }
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            String name = body.get("name");
            String email = body.get("email");
            String role = body.getOrDefault("role", "PATIENT");

            User user = switch (role) {
                case "DOCTOR" -> new Doctor(username, password, name, email);
                case "ADMIN"  -> new Admin(username, password, name, email);
                case "PATIENT" -> new Patient(username, password, name, email);
                default       -> throw new RoleNotFoundException(role + " is not allowed");
            };

            boolean success = userService.createUser(user);
            if (success) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.badRequest().body("Could not create user: " + username);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error : " + e.getMessage());
        }
    }

    /**
     * PUT /maclogixapi/v1/users/{username}
     * Update the information of an existing user.
     * Body JSON :
     * {
     *   "newUsername": "john456",
     *   "password":    "newpass",
     *   "name":        "John Updated",
     *   "email":       "john456@mail.com"
     * }
     */
    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody Map<String, String> body) {
        try {
//            String newUsername = body.getOrDefault("newUsername", username);
//            String password    = body.get("password");
//            String name        = body.get("name");
//            String email       = body.get("email");

            System.out.println("Updating user: "+ username);
            boolean success = userService.updateUser(username, body);

//            boolean success = userService.updateUser(username, newUsername, password, name, email);
            if (success) {
                return ResponseEntity.ok("User Updated successfully");
            }
            return ResponseEntity.badRequest().body("User not found or username already taken.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error : " + e.getMessage());
        }
    }

    /**
     * DELETE /maclogixapi/users/{username}
     * Delete a user by their username.
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
            return ResponseEntity.ok("User deleted : " + username);
        }
        return ResponseEntity.internalServerError().body("Error during deletion.");
    }
}
