package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.RegisterRequest;
import edu.secourse.patientportal.exception.InvalidInputException;
import edu.secourse.patientportal.exception.ResourceAlreadyExistsException;
import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import edu.secourse.patientportal.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service layer responsible for managing {@link User} entities.
 *
 * <p>This class provides core business logic for:
 * <ul>
 *     <li>User registration</li>
 *     <li>User creation and deletion</li>
 *     <li>User retrieval and updates</li>
 * </ul>
 *
 * <p><b>Data Source Behavior:</b>
 * <ul>
 *     <li>Uses {@link UserRepository} when running in Spring context</li>
 *     <li>Falls back to in-memory storage for unit testing</li>
 * </ul>
 *
 * <p><b>Security Features:</b>
 * <ul>
 *     <li>Passwords are hashed using {@link PasswordEncoder}</li>
 *     <li>User roles are normalized to uppercase</li>
 * </ul>
 */
@Service
public class UserService implements UserManagementService {

    /** JPA repository used for persistent storage. */
    @Autowired
    private UserRepository userRepository;

    /** Password encoder used to hash user passwords. */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /** In-memory fallback storage (used when repository is unavailable). */
    public ArrayList<User> users = new ArrayList<>();

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserService(){}

    /**
     * Registers a new user in the system.
     *
     * <p>This method validates input, ensures username uniqueness,
     * and creates a new {@link User} or {@link Admin} instance.
     *
     * @param registerRequest DTO containing registration details
     * @return the saved {@link User} entity
     * @throws InvalidInputException if username is missing or invalid
     * @throws ResourceAlreadyExistsException if username already exists
     */
    @Override
    public User register(RegisterRequest registerRequest) {

        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            throw new InvalidInputException(Constants.MSG_USERNAME_REQUIRED);
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceAlreadyExistsException(Constants.MSG_USERNAME_ALREADY_EXISTS);
        }

        User user;

        if ("ADMIN".equalsIgnoreCase(registerRequest.getRole())) {
            user = Admin.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .role("ADMIN")
                    .lastPasswordChange(LocalDateTime.now())
                    .build();
        } else {
            user = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .role(registerRequest.getRole().toUpperCase())
                    .lastPasswordChange(LocalDateTime.now())
                    .build();
        }

        return userRepository.save(user);
    }

    /**
     * Creates a new user if the username does not already exist.
     *
     * @param user the user to create
     * @return true if successfully created, false otherwise
     */
    public boolean createUser(User user) {
        try {
            if (userRepository != null) {
                if (!userRepository.existsByUsername(user.getUsername())) {
                    user.setPassword(hashPassword(user.getPassword()));
                    user.setRole(user.getRole().toUpperCase());
                    userRepository.save(user);
                    return true;
                }
            } else {
                for (User u : users) {
                    if (u.getUsername().equals(user.getUsername())) {
                        return false;
                    }
                }
                users.add(user);
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to search
     * @return the {@link User} if found, otherwise null
     */
    public User getUser(String username) {
        if (userRepository != null) {
            return userRepository.findByUsername(username).orElse(null);
        }
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all users.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        if (userRepository != null) {
            return userRepository.findAll();
        }
        return users;
    }

    /**
     * Hashes a raw password using the configured {@link PasswordEncoder}.
     *
     * @param rawPassword plaintext password
     * @return hashed password
     */
    private String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Removes a user from the system.
     *
     * @param user the user to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeUser(User user) {
        try {
            if (user == null) return false;

            if (userRepository != null) {
                userRepository.delete(user);
                return true;
            } else {
                return users.remove(user);
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Checks if a username already exists.
     *
     * @param username username to check
     * @return true if exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Updates a user's fields dynamically using a request body map.
     *
     * <p>Only fields present in the map will be updated.
     *
     * @param username existing username
     * @param body key-value map of fields to update
     * @return true if update successful, false if user not found
     */
    public boolean updateUser(String username, Map<String, String> body) {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        if (body.containsKey("username")) user.setUsername(body.get("username"));
        if (body.containsKey("email")) user.setEmail(body.get("email"));
        if (body.containsKey("password")) user.setPassword(hashPassword(body.get("password")));
        if (body.containsKey("last_name")) user.setLastName(body.get("last_name"));
        if (body.containsKey("first_name")) user.setFirstName(body.get("first_name"));
        if (body.containsKey("name")) user.setName(body.get("name"));

        if (body.containsKey("role")) {
            String role = body.get("role").toLowerCase();
            if (role.equals("doctor") || role.equals("patient") || role.equals("admin")) {
                user.setRole(role.toUpperCase());
            }
        }

        userRepository.save(user);
        return true;
    }

    /**
     * Checks if a given user exists in the system.
     *
     * @param user user to check
     * @return true if exists, false otherwise
     */
    public boolean containsUser(User user) {
        try {
            if (userRepository != null) {
                return userRepository.existsByUsername(user.getUsername());
            }
            return users.contains(user);
        } catch (Exception ignored) {}
        return false;
    }
}