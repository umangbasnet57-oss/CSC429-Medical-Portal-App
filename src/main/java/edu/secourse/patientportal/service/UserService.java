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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

/**
 * Service layer responsible for managing {@link User} objects.
 * <p>
 * Uses JPA repository when running in Spring Boot context.
 * Falls back to in-memory ArrayList for unit tests.
 */
public class UserService implements UserManagementService {

    /** JPA repository — injected by Spring, null when used in unit tests. */
    /** @Autowired //(required = false) */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** In-memory fallback list (used by unit tests). */
    public ArrayList<User> users = new ArrayList<>();


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserService(){}

    public User register(RegisterRequest registerRequest) {
        // Validate input
        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            throw new InvalidInputException(Constants.MSG_USERNAME_REQUIRED);
        }

        // Check if username already exists (409 Conflict)
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResourceAlreadyExistsException(Constants.MSG_USERNAME_ALREADY_EXISTS);
        }

//        // Check if email already exists (409 Conflict)
//        if (userRepository.existsByEmail(registerRequest.getEmail())) {
//            throw new ResourceAlreadyExistsException(Constants.MSG_EMAIL_ALREADY_EXISTS);
//        }
//        User user = createUser(registerRequest);

        // Create new user
        User user;
        if (registerRequest.getRole().equals("ADMIN")){
            user = Admin.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .role(registerRequest.getRole())
                    .lastPasswordChange(LocalDateTime.now())
                    .build();
            System.out.println("This user is an " + registerRequest.getRole());
        }
        else{
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

        // Save to repository (ID will be auto-generated)
        System.out.println("Will save user with role: " + registerRequest.getRole().toUpperCase());
        return userRepository.save(user);
    }

    /**
     * Attempts to create a new user. A user is only added if no existing user
     * already has the same username.
     *
     * @param user the user object to create
     * @return true if user successfully added, false otherwise
     */
    public boolean createUser(User user) {
        boolean success = false;
        try {
            if (userRepository != null) {
                if (!userRepository.existsByUsername(user.getUsername())) {
                    // hash user password
                    user.setPassword(hashPassword(user.getPassword()));
                    user.setRole(user.getRole().toUpperCase());
                    userRepository.save(user);
                    success = true;
                }
            } else {
                boolean exists = false;
                for (User value : users) {
                    if (value.getUsername().equals(user.getUsername())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    users.add(user);
                    success = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            e.printStackTrace();

        }
        return success;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username to search for
     * @return the matching user, or {@code null} if none found
     */
    public User getUser(String username) {
        if (userRepository != null) {
            return userRepository.findByUsername(username).orElse(null);
        }
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        if (userRepository != null) {
            return userRepository.findAll();
        }
        return users;
    }

    private String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Prints user details (account number, username, name, email) to console.
     * <p>
     * If the user does not exist, prints "User does not exist."
     *
     * @param username the username of the user to print
     */
    public void printUser(String username) {
        try {
            boolean userExists = false;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    System.out.println("\nAccount Number: " + user.getId());
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Name: " + user.getName());
                    System.out.println("Email: " + user.getEmail());
                    userExists = true;
                }
            }
            if (!userExists) {
                System.out.println("User does not exist.");
            }
        } catch (Exception ignored) {

        }
    }

    /**
     * Removes the given user object from the list if it exists.
     *
     * @param user the user object to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeUser(User user) {
        boolean success = false;
        try {
            if (user != null) {
                if (userRepository != null) {
                    userRepository.delete(user);
                    success = true;
                } else if (users.contains(user)) {
                    users.remove(user);
                    success = true;
                }
            }
        } catch (Exception ignored) {

        }
        return success;
    }

    /**
     * Checks if a user already exists in the database.
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

//    public boolean updateUser(String oldUsername, String newUsername, String hashedPassword, String name, String email) {
//        boolean success = false;
//        try {
//            if (userRepository != null) {
//                System.out.println("user repo is NUll");
//                if (!oldUsername.equals(newUsername) && userRepository.existsByUsername(newUsername)) {
//                    System.out.println("user kinda exists.");
//                    return false;
//                }
//                User user = userRepository.findByUsername(oldUsername).orElse(null);
//                if (user != null) {
//                    user.setUsername(newUsername);
//                    user.setPassword(hashedPassword);
//                    user.setName(name);
//                    user.setEmail(email);
//                    userRepository.save(user);
//                    success = true;
//                }
//            } else {
//                if (!oldUsername.equals(newUsername)) {
//                    for (User user : users) {
//                        if (user.getUsername().equals(newUsername)) {
//                            System.out.println("user also kinda exists");
//                            return false;
//                        }
//                    }
//                }
//                for (User user : users) {
//                    if (user.getUsername().equals(oldUsername)) {
//                        user.setUsername(newUsername);
//                        user.setPassword(hashedPassword);
//                        user.setName(name);
//                        user.setEmail(email);
//                        success = true;
//                    }
//                }
//            }
//        } catch (Exception ignored) {
//
//        }
//        return success;
//    }
    public boolean updateUser(String username, Map<String, String> body) {

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        if (body.containsKey("username")){
            user.setUsername(body.get("username"));
        }

        if (body.containsKey("email")) {
            user.setEmail(body.get("email"));
        }

        if (body.containsKey("password")) {
            user.setPassword(hashPassword(body.get("password")));
        }

        if (body.containsKey("last_name")){
            user.setLastName(body.get("last_name"));
        }

        if (body.containsKey("first_name")){
            user.setFirstName(body.get("first_name"));
        }

        if (body.containsKey("name")) {
            user.setName(body.get("name"));
        }

        if (body.containsKey("role")){
            String roleToUpdate = body.get("role").toLowerCase();
            if (roleToUpdate.equals("doctor") | roleToUpdate.equals("patient") | roleToUpdate.equals("admin")){
                user.setRole(body.get("role"));
            }
        }

        userRepository.save(user);

        return true;
    }

    /**
     * Checks whether a given user exists in the internal user list.
     *
     * @param user the user object to look for
     * @return true if user exists, false otherwise
     */
    public boolean containsUser(User user) {
        boolean containsUser = false;
        try {
            if (userRepository != null) {
                containsUser = userRepository.existsByUsername(user.getUsername());
            } else {
                containsUser = users.contains(user);
            }
        } catch (Exception ignored) {

        }
        return containsUser;
    }
}

