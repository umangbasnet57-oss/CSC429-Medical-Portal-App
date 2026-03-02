package edu.patientportal.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.secourse.patientportal.models.Doctor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Doctor} class.
 *
 * <p>This suite validates core features of the Doctor model:
 * <ul>
 *     <li>Constructor initialization</li>
 *     <li>Setter methods inherited from {@code User}</li>
 *     <li>Equality and hashCode behavior based on username</li>
 *     <li>Self-equality, null comparison, and type-safety</li>
 *     <li>Auto-incrementing account numbers</li>
 *     <li>Doctor ID getter functionality</li>
 * </ul>
 *
 * <p>These tests rely on actual {@link Doctor} objects and perform
 * field-level verification without mocks.
 */
public class DoctorTest {

    /**
     * Verifies that the {@link Doctor} constructor initializes
     * all inherited and local fields correctly.
     */
    @Test
    @DisplayName("constructorTest(): constructor test")
    void constructorTest() {
        // Arrange
        Doctor doctor = new Doctor("admin", "hash", "Admin Name", "admin@mail.com");

        // Act & Assert
        assertEquals("admin", doctor.getUsername());
        assertEquals("hash", doctor.getHashedPassword());
        assertEquals("Admin Name", doctor.getName());
        assertEquals("admin@mail.com", doctor.getEmail());
        assertTrue(doctor.getAccountNumber() > 0);
    }

    /**
     * Ensures that the setter methods correctly update all modifiable
     * fields inherited from {@link edu.secourse.patientportal.models.User}.
     */
    @Test
    @DisplayName("setterMethodsTest(): setter methods test")
    void setterMethodsTest() {
        // Arrange & Act
        Doctor doctor = new Doctor("admin", "hash", "Admin", "admin@mail.com");
        doctor.setUsername("newAdmin");
        doctor.setHashedPassword("newHash");
        doctor.setName("New Name");
        doctor.setEmail("new@mail.com");
        doctor.setRole("adminRole");

        // Assert
        assertEquals("newAdmin", doctor.getUsername());
        assertEquals("newHash", doctor.getHashedPassword());
        assertEquals("New Name", doctor.getName());
        assertEquals("new@mail.com", doctor.getEmail());
        assertEquals("adminRole", doctor.getRole());
    }

    /**
     * Confirms that two {@link Doctor} objects with identical usernames
     * are considered equal and produce matching hash codes.
     */
    @Test
    @DisplayName("equalsSameUsernameTest(): same usernames test")
    void equalsSameUsernameTest() {
        // Arrange & Act
        Doctor d1 = new Doctor("sameUser", "p1", "N1", "e1@mail.com");
        Doctor d2 = new Doctor("sameUser", "p2", "N2", "e2@mail.com");

        // Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    /**
     * Validates that two {@link Doctor} instances with different usernames
     * are not equal and produce different hash codes.
     */
    @Test
    @DisplayName("equalsDifferentUsernameTest(): different usernames test")
    void equalsDifferentUsernameTest() {
        // Arrange & Act
        Doctor d1 = new Doctor("user1", "p1", "N1", "e1@mail.com");
        Doctor d2 = new Doctor("user2", "p2", "N2", "e2@mail.com");

        // Assert
        assertNotEquals(d1, d2);
        assertNotEquals(d1.hashCode(), d2.hashCode());
    }

    /**
     * Verifies that an object equals itself (reflexive equality rule).
     */
    @Test
    @DisplayName("equalsSelfTest(): object equals self test")
    void equalsSelfTest() {
        // Arrange & Act
        Doctor doctor = new Doctor("user", "p", "Name", "mail@mail.com");

        // Assert
        assertEquals(doctor, doctor);
    }

    /**
     * Ensures that comparisons against {@code null} or different object types
     * correctly return false, preserving type-safety.
     */
    @Test
    @DisplayName("equalsInvalidTest(): equal invalid test")
    void equalsInvalidTest() {
        // Arrange & Act
        Doctor doctor = new Doctor("user", "p", "Name", "mail@mail.com");

        // Assert
        assertNotEquals(doctor, null);
        assertNotEquals(doctor, "notUser");
    }

    /**
     * Confirms that account numbers increment across
     * {@link Doctor} object instantiation.
     */
    @Test
    @DisplayName("accountNumberAutoIncrementTest(): account numbers increase test")
    void accountNumberAutoIncrementTest() {
        // Arrange
        Doctor d1 = new Doctor("u1", "p1", "N1", "e1@mail.com");
        Doctor d2 = new Doctor("u2", "p2", "N2", "e2@mail.com");

        // Act
        int id1 = d1.getAccountNumber();
        int id2 = d2.getAccountNumber();

        // Assert
        assertTrue(id2 > id1);
    }

    /**
     * Tests the getter for doctorId to ensure it provides
     * consistent values.
     */
    @Test
    @DisplayName("getDoctorIdTest(): get doctor id test")
    void getDoctorIdTest() {
        // Arrange & Act
        Doctor doctor = new Doctor("u2", "p2", "N2", "e2@mail.com");

        // Assert
        assertEquals(doctor.getDoctorId(), doctor.getDoctorId());
    }
}

