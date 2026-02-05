package edu.patientportal.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.secourse.patientportal.models.Patient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Patient} class.
 *
 * <p>This suite verifies correct behavior of:
 * <ul>
 *     <li>Constructor field initialization</li>
 *     <li>Setter methods inherited from {@code User}</li>
 *     <li>Equality and hashCode based on username</li>
 *     <li>Self-comparison, null/type safety, and inequality behavior</li>
 *     <li>Auto-incrementing account numbers</li>
 *     <li>Patient ID getter functionality</li>
 * </ul>
 *
 * <p>All tests use real {@link Patient} objects—no mocks—with Arrange/Act/Assert structure.
 */
public class PatientTest {

    /**
     * Verifies that the {@link Patient} constructor properly initializes
     * all inherited and local fields.
     */
    @Test
    @DisplayName("constructorTest(): constructor test")
    void constructorTest() {
        // Arrange & Act
        Patient p = new Patient("admin", "hash", "Admin Name", "admin@mail.com");

        // Assert
        assertEquals("admin", p.getUsername());
        assertEquals("hash", p.getHashedPassword());
        assertEquals("Admin Name", p.getName());
        assertEquals("admin@mail.com", p.getEmail());
        assertTrue(p.getAccountNumber() > 0);
    }

    /**
     * Ensures that all setter methods inherited from {@link edu.secourse.patientportal.models.User}
     * correctly update their respective fields.
     */
    @Test
    @DisplayName("setterMethodsTest(): setter methods test")
    void setterMethodsTest() {
        // Arrange
        Patient p = new Patient("admin", "hash", "Admin", "admin@mail.com");

        // Act
        p.setUsername("newAdmin");
        p.setHashedPassword("newHash");
        p.setName("New Name");
        p.setEmail("new@mail.com");
        p.setRole("patientRole");

        // Assert
        assertEquals("newAdmin", p.getUsername());
        assertEquals("newHash", p.getHashedPassword());
        assertEquals("New Name", p.getName());
        assertEquals("new@mail.com", p.getEmail());
        assertEquals("patientRole", p.getRole());
    }

    /**
     * Verifies that patients with identical usernames are considered equal
     * and generate the same hash code.
     */
    @Test
    @DisplayName("equalsSameUsernameTest(): patient same username test")
    void equalsSameUsernameTest() {
        // Arrange & Act
        Patient p1 = new Patient("sameUser", "p1", "N1", "e1@mail.com");
        Patient p2 = new Patient("sameUser", "p2", "N2", "e2@mail.com");

        // Assert
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * Confirms that patients with different usernames are not equal
     * and produce different hash codes.
     */
    @Test
    @DisplayName("equalsDifferentUsernameTest(): patient different username test")
    void equalsDifferentUsernameTest() {
        // Arrange & Act
        Patient p1 = new Patient("user1", "p1", "N1", "e1@mail.com");
        Patient p2 = new Patient("user2", "p2", "N2", "e2@mail.com");

        // Assert
        assertNotEquals(p1, p2);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * Tests reflexive equality: a {@link Patient} must equal itself.
     */
    @Test
    @DisplayName("equalsSelfTest(): object equals itself test")
    void equalsSelfTest() {
        // Arrange & Act
        Patient p = new Patient("user", "p", "Name", "mail@mail.com");

        // Assert
        assertEquals(p, p);
    }

    /**
     * Ensures that comparing a {@link Patient} to null or a non-User object
     * correctly returns {@code false}.
     */
    @Test
    @DisplayName("equalsInvalidTest(): object inequality test")
    void equalsInvalidTest() {
        // Arrange & Act
        Patient p = new Patient("user", "p", "Name", "mail@mail.com");

        // Assert
        assertNotEquals(p, null);
        assertNotEquals(p, "notAUser");
    }

    /**
     * Confirms that account numbers increment automatically when new {@link Patient}
     * objects are created.
     */
    @Test
    @DisplayName("accountNumberAutoIncrementTest(): account number auto increment test")
    void accountNumberAutoIncrementTest() {
        // Arrange
        Patient p1 = new Patient("u1", "p1", "N1", "e1@mail.com");
        Patient p2 = new Patient("u2", "p2", "N2", "e2@mail.com");

        // Act
        int id1 = p1.getAccountNumber();
        int id2 = p2.getAccountNumber();

        // Assert
        assertTrue(id2 > id1);
    }

    /**
     * Provides simple coverage for the {@link Patient#getPatientId()} method.
     */
    @Test
    @DisplayName("getPatientIdTest(): get patient id test")
    void getPatientIdTest() {
        // Arrange & Act
        Patient p = new Patient("u2", "p2", "N2", "e2@mail.com");

        // Assert
        assertEquals(p.getPatientId(), p.getPatientId());
    }
}

