package edu.patientportal.services;

import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Admin;
import edu.secourse.patientportal.services.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UserService} class.
 *
 * <p>This test suite validates the following behaviors:
 * <ul>
 *     <li>Creating new users and retrieving them by username</li>
 *     <li>Removing existing users</li>
 *     <li>Fetching specific users via {@code getUser}</li>
 *     <li>Printing user details to console output</li>
 *     <li>Printing correct messages when a user does not exist</li>
 * </ul>
 *
 * <p>All tests follow the Arrange–Act–Assert pattern and use live instances of
 * {@link UserService} and real model objects.</p>
 */
public class UserServiceTest {

    /**
     * Verifies that multiple different users (Patient, Admin, Doctor)
     * can be created and then retrieved successfully by username.
     */
    @Test
    @DisplayName("createUser(): create a user")
    public void createUserTest() {
        // Arrange
        UserService userService = new UserService();
        User patient = new Patient("jfox", "lskjdlfjsdj", "John Fox", "johnfox8@gmail.com");
        User admin   = new Admin("bjones", "sdlfkjlsj", "Brian Jones", "brianjones@gmail.com");
        User doctor  = new Doctor("rraux", "lkmlksljdioj", "Raul Rox", "raulrox23@gmail.com");

        // Act
        userService.createUser(patient);
        userService.createUser(admin);
        userService.createUser(doctor);

        // Assert
        assertEquals(patient, userService.getUser("jfox"));
        assertEquals(admin,   userService.getUser("bjones"));
        assertEquals(doctor,  userService.getUser("rraux"));
    }

    /**
     * Tests that removing users successfully deletes them from storage,
     * and that subsequent calls to {@code getUser} no longer return them.
     */
    @Test
    @DisplayName("removeUser(): remove a user")
    public void removeUserTest() {
        // Arrange
        UserService userService = new UserService();
        User patient = new Patient("jfox", "lskjdlfjsdj", "John Fox", "johnfox8@gmail.com");
        User admin   = new Admin("bjones", "sdlfkjlsj", "Brian Jones", "brianjones@gmail.com");
        User doctor  = new Doctor("rraux", "lkmlksljdioj", "Raul Rox", "raulrox23@gmail.com");

        // Act
        userService.createUser(patient);
        userService.createUser(admin);
        userService.createUser(doctor);

        userService.removeUser(patient);
        userService.removeUser(admin);
        userService.removeUser(doctor);

        // Assert
        assertNotEquals(patient, userService.getUser(patient.getUsername()));
    }

    /**
     * Ensures that a created user can be fetched correctly using its username.
     */
    @Test
    @DisplayName("getUser(): get a user")
    public void getUserTest() {
        // Arrange
        UserService userService = new UserService();
        User patient = new Patient("jfox", "lskjdlfjsdj", "John Fox", "johnfox8@gmail.com");

        // Act
        userService.createUser(patient);

        // Assert
        assertEquals(patient, userService.getUser(patient.getUsername()));
    }

    /**
     * Captures console output and verifies that {@code printUser}
     * correctly prints stored user information.
     */
    @Test
    @DisplayName("printUser(): print user details")
    public void printUserTest() {
        // Arrange
        UserService service = new UserService();
        User user = new Patient("john123", "pass", "John Smith", "john@mail.com");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act
        service.createUser(user);
        System.setOut(new PrintStream(out));
        service.printUser("john123");

        String output = out.toString();

        // Assert
        assertTrue(output.contains("Account Number"));
        assertTrue(output.contains("john123"));
        assertTrue(output.contains("John Smith"));
        assertTrue(output.contains("john@mail.com"));
        assertFalse(output.contains("User does not exist."));
    }

    /**
     * Verifies that attempting to print a non-existent user results in:
     * <ul>
     *     <li>No unwanted user details</li>
     *     <li>The expected "User does not exist." message</li>
     * </ul>
     */
    @Test
    @DisplayName("printUserDoesntExist(): print user doesn't exist")
    public void printUserDoesntExistTest() {
        // Arrange
        UserService service = new UserService();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act
        System.setOut(new PrintStream(out));
        service.printUser("john123");

        String output = out.toString();

        // Assert
        assertFalse(output.contains("john123"));
        assertFalse(output.contains("John Smith"));
        assertFalse(output.contains("john@mail.com"));
        assertTrue(output.contains("User does not exist."));
    }
}

