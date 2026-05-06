package edu.secourse.patientportal.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorShouldCreateUser() {
        User user = new User(
                "jdoe",
                "password123",
                "John Doe",
                "jdoe@example.com",
                "patient"
        );

        assertEquals("jdoe", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("John Doe", user.getName());
        assertEquals("jdoe@example.com", user.getEmail());
        assertEquals("PATIENT", user.getRole());
    }

    @Test
    void constructorShouldSplitFullNameIntoFirstAndLastName() {
        User user = new User(
                "jdoe",
                "password",
                "John Doe",
                "jdoe@example.com",
                "patient"
        );

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void constructorShouldHandleSingleName() {
        User user = new User(
                "jdoe",
                "password",
                "John",
                "jdoe@example.com",
                "patient"
        );

        assertEquals("John", user.getFirstName());
        assertEquals("", user.getLastName());
    }

    @Test
    void getIdShouldReturnZeroWhenIdIsNull() {
        User user = new User();

        assertEquals(0, user.getId());
    }

    @Test
    void setUsernameShouldUpdateUsername() {
        User user = new User();

        user.setUsername("asmith");

        assertEquals("asmith", user.getUsername());
    }

    @Test
    void setPasswordShouldUpdatePasswordAndLastPasswordChange() {
        User user = new User();

        assertNull(user.getLastPasswordChange());

        user.setPassword("newPassword");

        assertEquals("newPassword", user.getPassword());
        assertNotNull(user.getLastPasswordChange());
    }

    @Test
    void setNameShouldUpdateNameAndSplitFirstAndLastName() {
        User user = new User();

        user.setName("Alice Smith");

        assertEquals("Alice Smith", user.getName());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void setNameShouldHandleSingleName() {
        User user = new User();

        user.setName("Alice");

        assertEquals("Alice", user.getName());
        assertEquals("Alice", user.getFirstName());
        assertEquals("", user.getLastName());
    }

    @Test
    void setEmailShouldUpdateEmail() {
        User user = new User();

        user.setEmail("new@example.com");

        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void setFirstNameShouldUpdateFirstName() {
        User user = new User();

        user.setFirstName("John");

        assertEquals("John", user.getFirstName());
    }

    @Test
    void setLastNameShouldUpdateLastName() {
        User user = new User();

        user.setLastName("Doe");

        assertEquals("Doe", user.getLastName());
    }

    @Test
    void recordLoginShouldSetLastLogin() {
        User user = new User();

        assertNull(user.getLastLogin());

        user.recordLogin();

        assertNotNull(user.getLastLogin());
        assertTrue(user.getLastLogin().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void setRoleShouldStoreRoleAsUpperCase() {
        User user = new User();

        user.setRole("doctor");

        assertEquals("DOCTOR", user.getRole());
    }

    @Test
    void equalsShouldReturnTrueForSameUsername() {
        User user1 = new User(
                "jdoe",
                "password1",
                "John Doe",
                "jdoe1@example.com",
                "patient"
        );

        User user2 = new User(
                "jdoe",
                "password2",
                "Jane Doe",
                "jdoe2@example.com",
                "doctor"
        );

        assertEquals(user1, user2);
    }

    @Test
    void equalsShouldReturnFalseForDifferentUsername() {
        User user1 = new User(
                "jdoe",
                "password1",
                "John Doe",
                "jdoe@example.com",
                "patient"
        );

        User user2 = new User(
                "asmith",
                "password2",
                "Alice Smith",
                "asmith@example.com",
                "doctor"
        );

        assertNotEquals(user1, user2);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        User user = new User();

        assertNotEquals(null, user);
    }

    @Test
    void hashCodeShouldBeSameForSameUsername() {
        User user1 = new User(
                "jdoe",
                "password1",
                "John Doe",
                "jdoe1@example.com",
                "patient"
        );

        User user2 = new User(
                "jdoe",
                "password2",
                "Jane Doe",
                "jdoe2@example.com",
                "doctor"
        );

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toStringShouldContainUserDetails() {
        User user = new User(
                "jdoe",
                "password",
                "John Doe",
                "jdoe@example.com",
                "patient"
        );

        String result = user.toString();

        assertTrue(result.contains("jdoe"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("jdoe@example.com"));
        assertTrue(result.contains("PATIENT"));
    }
}