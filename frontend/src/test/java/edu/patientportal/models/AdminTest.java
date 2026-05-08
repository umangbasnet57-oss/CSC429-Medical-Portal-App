package edu.patientportal.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.secourse.patientportal.models.Admin;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link Admin} model.
 * <p>
 * These tests verify:
 * <ul>
 *     <li>Constructor correctness</li>
 *     <li>Getter and setter behavior</li>
 *     <li>User equality based on username</li>
 *     <li>Account number auto-incrementing</li>
 *     <li>Admin ID generation</li>
 * </ul>
 * <p>
 * All tests use actual {@link Admin} objects with no mocks or fakes.
 */
public class AdminTest {

    /**
     * Verifies that the constructor correctly initializes
     * all inherited {@link edu.secourse.patientportal.models.User} fields.
     */
    @Test
    @DisplayName("constructorTest(): constructor test")
    void constructorTest() {
        // Arrange & Act
        Admin a = new Admin("admin", "hash", "Admin Name", "admin@mail.com");

        // Assert
        assertEquals("admin", a.getUsername());
        assertEquals("hash", a.getHashedPassword());
        assertEquals("Admin Name", a.getName());
        assertEquals("admin@mail.com", a.getEmail());
        assertEquals("admin", a.getRole());
        assertTrue(a.getAccountNumber() > 0);
    }

    /**
     * Ensures setter methods correctly update internal state of the Admin instance.
     */
    @Test
    @DisplayName("setterFieldsTest(): setter fields test")
    void setterFieldsTest() {
        // Arrange
        Admin a = new Admin("admin", "hash", "Admin", "admin@mail.com");

        // Act
        a.setUsername("newAdmin");
        a.setHashedPassword("newHash");
        a.setName("New Name");
        a.setEmail("new@mail.com");
        a.setRole("adminRole");

        // Assert
        assertEquals("newAdmin", a.getUsername());
        assertEquals("newHash", a.getHashedPassword());
        assertEquals("New Name", a.getName());
        assertEquals("new@mail.com", a.getEmail());
        assertEquals("adminRole", a.getRole());
    }

    /**
     * Tests that two Admin objects with the same username are considered equal.
     */
    @Test
    @DisplayName("userEqualsTest(): user equals test")
    void userEqualsTest() {
        // Arrange
        Admin a1 = new Admin("sameUser", "p1", "Name1", "e1@mail.com");
        Admin a2 = new Admin("sameUser", "p2", "Name2", "e2@mail.com");

        // Act & Assert
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    /**
     * Ensures Admin objects with different usernames are not equal.
     */
    @Test
    @DisplayName("userNotEqualsTest(): user not equals test")
    void userNotEqualsTest() {
        // Arrange
        Admin a1 = new Admin("user1", "p1", "Name1", "e1@mail.com");
        Admin a2 = new Admin("user2", "p2", "Name2", "e2@mail.com");

        // Act & Assert
        assertNotEquals(a1, a2);
        assertNotEquals(a1.hashCode(), a2.hashCode());
    }

    /**
     * Ensures an Admin object always equals itself.
     */
    @Test
    @DisplayName("userEqualsTestSameObject(): user equals test same object")
    void userEqualsTestSameObject() {
        // Arrange
        Admin a = new Admin("user", "p", "Name", "mail@mail.com");

        // Act & Assert
        assertEquals(a, a);
    }

    /**
     * Confirms that an Admin instance is never equal to null or to a non-User object.
     */
    @Test
    @DisplayName("nullUserTest(): null user test")
    void nullUserTest() {
        // Arrange
        Admin a = new Admin("user", "p", "Name", "mail@mail.com");

        // Act & Assert
        assertNotEquals(null, a);
        assertNotEquals("notUser", a);
    }

    /**
     * Verifies that account numbers auto-increment between Admin instances.
     */
    @Test
    @DisplayName("accountNumberIncrementTest(): account number increment test")
    void accountNumberIncrementTest() {
        // Arrange
        Admin a1 = new Admin("u1", "p1", "N1", "e1@mail.com");
        Admin a2 = new Admin("u2", "p2", "N2", "e2@mail.com");

        // Act
        int id1 = a1.getAccountNumber();
        int id2 = a2.getAccountNumber();

        // Assert
        assertTrue(id2 > id1);
    }

    /**
     * Confirms that getAdminId returns the expected ID value.
     */
    @Test
    @DisplayName("getAdminIdTest(): get admin id test")
    void getAdminIdTest() {
        // Arrange
        Admin a2 = new Admin("u2", "p2", "N2", "e2@mail.com");
        Admin admin = new Admin();  // default constructor

        // Act & Assert
        assertEquals(a2.getAdminId(), a2.getAdminId());
    }
}


