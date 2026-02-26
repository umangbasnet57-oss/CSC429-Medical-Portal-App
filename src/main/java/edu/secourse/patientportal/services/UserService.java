package edu.secourse.patientportal.services;

import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

/**
 * Service layer responsible for managing {@link User} objects.
 * <p>
 * Uses JPA repository when running in Spring Boot context.
 * Falls back to in-memory ArrayList for unit tests.
 */
public class UserService {

    /** JPA repository — injected by Spring, null when used in unit tests. */
    @Autowired(required = false)
    private UserRepository userRepository;

    /** In-memory fallback list (used by unit tests). */
    public ArrayList<User> users = new ArrayList<>();

    /**
     * Default constructor. Initializes an empty user list.
     */
    public UserService() {

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
        } catch (Exception ignored) {

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
                    System.out.println("\nAccount Number: " + user.getAccountNumber());
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
     * Updates an existing user's username, password, name, and email.
     * <p>
     * If {@code newUsername} is already taken by a different user, the update
     * is rejected and {@code false} is returned.
     *
     * @param oldUsername   the user's current username
     * @param newUsername   the user's desired new username
     * @param hashedPassword the new hashed password
     * @param name           the updated name
     * @param email          the updated email
     * @return true if update succeeded, false otherwise
     */
    public boolean updateUser(String oldUsername, String newUsername, String hashedPassword, String name, String email) {
        boolean success = false;
        try {
            if (userRepository != null) {
                if (!oldUsername.equals(newUsername) && userRepository.existsByUsername(newUsername)) {
                    return false;
                }
                User user = userRepository.findByUsername(oldUsername).orElse(null);
                if (user != null) {
                    user.setUsername(newUsername);
                    user.setHashedPassword(hashedPassword);
                    user.setName(name);
                    user.setEmail(email);
                    userRepository.save(user);
                    success = true;
                }
            } else {
                if (!oldUsername.equals(newUsername)) {
                    for (User user : users) {
                        if (user.getUsername().equals(newUsername)) {
                            return false;
                        }
                    }
                }
                for (User user : users) {
                    if (user.getUsername().equals(oldUsername)) {
                        user.setUsername(newUsername);
                        user.setHashedPassword(hashedPassword);
                        user.setName(name);
                        user.setEmail(email);
                        success = true;
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return success;
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

