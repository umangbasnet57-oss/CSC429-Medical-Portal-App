package edu.secourse.patientportal.routes;

import edu.secourse.patientportal.models.Admin;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST routes for user management.
 * Base URL: /api/users
 */
@RestController
@RequestMapping("/api/users")
public class UserRoutes {

    private final UserService userService;

    public UserRoutes(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users/{username}
     * Retourne les informations d'un utilisateur par son username.
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = userService.getUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * POST /api/users
     * Crée un nouvel utilisateur.
     * Body JSON :
     * {
     *   "username": "john123",
     *   "password": "pass123",
     *   "name":     "John Smith",
     *   "email":    "john@mail.com",
     *   "role":     "patient"  (patient | doctor | admin)
     * }
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String password = body.get("password");
            String name     = body.get("name");
            String email    = body.get("email");
            String role     = body.getOrDefault("role", "patient").toLowerCase();

            User user = switch (role) {
                case "doctor" -> new Doctor(username, password, name, email);
                case "admin"  -> new Admin(username, password, name, email);
                default       -> new Patient(username, password, name, email);
            };

            boolean success = userService.createUser(user);
            if (success) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.badRequest().body("Username déjà utilisé : " + username);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    /**
     * PUT /api/users/{username}
     * Met à jour les informations d'un utilisateur existant.
     * Body JSON :
     * {
     *   "newUsername": "john456",
     *   "password":    "newpass",
     *   "name":        "John Updated",
     *   "email":       "john456@mail.com"
     * }
     */
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody Map<String, String> body) {
        try {
            String newUsername = body.getOrDefault("newUsername", username);
            String password    = body.get("password");
            String name        = body.get("name");
            String email       = body.get("email");

            boolean success = userService.updateUser(username, newUsername, password, name, email);
            if (success) {
                return ResponseEntity.ok("Utilisateur mis à jour : " + newUsername);
            }
            return ResponseEntity.badRequest().body("Utilisateur introuvable ou username déjà pris.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    /**
     * DELETE /api/users/{username}
     * Supprime un utilisateur par son username.
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        User user = userService.getUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        boolean success = userService.removeUser(user);
        if (success) {
            return ResponseEntity.ok("Utilisateur supprimé : " + username);
        }
        return ResponseEntity.internalServerError().body("Erreur lors de la suppression.");
    }
}
