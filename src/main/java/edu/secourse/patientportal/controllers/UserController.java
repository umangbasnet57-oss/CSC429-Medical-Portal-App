package edu.secourse.patientportal.controllers;

import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.services.UserService;

/**
 * Controller responsible for handling operations related to User entities.
 * <p>
 * This class serves as the interface between the UI layer and the UserService.
 * All method calls are wrapped in try-catch blocks to prevent program crashes
 * caused by unexpected user input or invalid service states.
 */
public class UserController {

    private UserService userService = new UserService();

    /**
     * Default constructor that initializes a new UserService instance.
     */
    public UserController(){

    }

    /**
     * Creates a UserController with a provided UserService instance.
     * If the passed service is null, the internal default UserService remains in use.
     *
     * @param userService the UserService to associate with this controller
     */
    public UserController(UserService userService) {
        try {
            if (userService != null && this.userService != null) {
                this.userService = userService;
            }
        } catch (Exception _) {

        }
    }

    /**
     * Attempts to create a user using the underlying UserService.
     *
     * @param user the User object to be created
     * @return true if creation succeeded, false otherwise
     */
    public boolean createUser(User user) {
        boolean success = false;
        try {
            if (user != null) {
                success = userService.createUser(user);
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Updates an existing user's information using the UserService.
     *
     * @param oldUsername   the user's current username
     * @param newUsername   the desired new username
     * @param hashedPassword the updated password (hashed)
     * @param name           the updated display name
     * @param email          the updated email address
     * @return true if update succeeded, false otherwise
     */
    public boolean updateUser(String oldUsername, String newUsername, String hashedPassword, String name, String email) {
        boolean success = false;
        try {
            success = userService.updateUser(oldUsername, newUsername, hashedPassword, name, email);
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Validates whether a given user exists in the UserService.
     *
     * @param user the User object to validate
     * @return true if the user exists and matches, false otherwise
     */
    public boolean validateUser(User user) {
        boolean isValid = false;
        try {
            if (user != null) {
                if (userService.containsUser(user)) {
                    isValid = true;
                }
            }
        } catch (Exception _) {

        }
        return isValid;
    }
}

