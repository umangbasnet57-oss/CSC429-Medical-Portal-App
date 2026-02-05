package edu.secourse.patientportal.services;
import edu.secourse.patientportal.models.User;

import java.util.ArrayList;

/**
 * Service layer responsible for managing {@link User} objects.
 * <p>
 * This class stores all users in memory and provides operations for creating,
 * retrieving, printing, updating, and removing users.
 * <p>
 * All methods follow a defensive design using try-catch blocks so that any
 * UI or controller interaction cannot crash the application.
 */
public class UserService {

    /** Internal list of all registered users in the system. */
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
        } catch (Exception _) {

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
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
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
        } catch (Exception _) {

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
                if (users.contains(user)) {
                    users.remove(user);
                    success = true;
                }
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Updates an existing user's username, password, name, and email.
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
            // Find matching user
            for (User user : users) {
                if (user.getUsername().equals(oldUsername)) {
                    user.setUsername(newUsername);
                    user.setHashedPassword(hashedPassword);
                    user.setName(name);
                    user.setEmail(email);

                    success = true;
                }
            }
        } catch (Exception _) {

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
            if (users.contains(user)) {
                containsUser = true;
            }
        } catch (Exception _) {

        }
        return containsUser;
    }
}

