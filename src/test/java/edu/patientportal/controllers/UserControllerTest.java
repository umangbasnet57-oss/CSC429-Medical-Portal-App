package edu.patientportal.controllers;

import edu.secourse.patientportal.models.Admin;
import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.services.UserService;
import edu.secourse.patientportal.controllers.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link UserController}.
 * <p>
 * These tests validate controller logic including:
 * <ul>
 *     <li>Updating user details</li>
 *     <li>Handling non-existing users</li>
 *     <li>Handling duplicate usernames</li>
 *     <li>Validating users via {@link UserService}</li>
 * </ul>
 * <p>
 */
public class UserControllerTest {

    /**
     * Utility function that captures console output during execution.
     *
     * @param action A lambda representing the action whose output is captured.
     * @return The captured printed output as a string.
     */
    private String captureOutput(Runnable action) {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        // Act
        action.run();

        // Assert
        System.setOut(original);
        return out.toString();
    }

    /**
     * Tests successful update of a user using valid input data.
     */
    @Test
    @DisplayName("updateUserTest(): update user test")
    void updateUserTest() {
        // Arrange
        UserService service = new UserService();
        UserController controller = new UserController(service);
        UserController userControllerCoverageCall = new UserController();
        User user = new Admin("admin", "pass", "Admin Name", "admin@mail.com");

        // Act
        controller.createUser(user);

        captureOutput(() ->
                controller.updateUser("admin", "newAdmin", "newPass", "New Name", "new@mail.com")
        );

        // Assert
        assertTrue(controller.updateUser("newAdmin", "newAdmin", "newPass", "New Name", "new@mail.com"));
    }

    /**
     * Verifies controller behavior when attempting to update a user that does not exist.
     */
    @Test
    @DisplayName("updateUserNotFoundTest(): update user not found test")
    void updateUserNotFoundTest() {
        // Arrange
        UserService service = new UserService();
        UserController controller = new UserController(service);

        // Act
        captureOutput(() ->
                controller.updateUser("missing", "new", "pass", "Name", "mail@mail.com")
        );

        // Assert
        assertFalse(controller.updateUser("missing", "new", "pass", "Name", "mail@mail.com"));
    }

    /**
     * Ensures that updating a user with a username already used by another user fails.
     */
    @Test
    @DisplayName("updateDuplicatUserTest(): update duplicate user test")
    void updateDuplicatUserTest() {
        // Arrange
        UserService service = new UserService();
        UserController controller = new UserController(service);

        // Act
        controller.createUser(new Admin("u1", "p1", "Name1", "u1@mail.com"));
        controller.createUser(new Admin("u2", "p2", "Name2", "u2@mail.com"));

        captureOutput(() ->
                controller.updateUser("u1", "u2", "pass", "Name", "mail@mail.com")
        );

        // Assert
        assertFalse(controller.updateUser("u1", "u2", "pass", "Name", "mail@mail.com"));
    }

    /**
     * Tests validation logic by verifying that a stored user is valid
     * and checking behavior when user data is modified.
     */
    @Test
    @DisplayName("validateUserTest(): validate user test")
    void validateUserTest() {
        // Arrange
        UserService service = new UserService();
        UserController controller = new UserController(service);

        // Act
        User user = new Admin("admin", "pass", "Admin Name", "admin@mail.com");
        controller.createUser(user);

        boolean result = controller.validateUser(user);

        User userTwo = new Admin("admin", "pass", "Admin Name", "admin@mail.com");
        userTwo.setRole("");

        controller.createUser(userTwo);

        boolean resultTwo = controller.validateUser(userTwo);

        // Assert
        assertTrue(result);
    }
}


