package edu.secourse.patientportal.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void defaultConstructorShouldSetDefaultValues() {
        User user = new User();

        assertEquals(0, user.getId());
        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals("", user.getName());
        assertEquals("", user.getEmail());
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("", user.getRole());
        assertNull(user.getLastLogin());
        assertNull(user.getLastPasswordChange());
    }

    @Test
    void constructorShouldSetAllFieldsAndSplitFullName() {
        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "patient"
        );

        assertEquals("john", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("John Smith", user.getName());
        assertEquals("john@email.com", user.getEmail());
        assertEquals("PATIENT", user.getRole());
        assertEquals("John", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void constructorShouldHandleSingleName() {
        User user = new User(
                "mary",
                "password",
                "Mary",
                "mary@email.com",
                "doctor"
        );

        assertEquals("Mary", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("DOCTOR", user.getRole());
    }

    @Test
    void constructorShouldHandleBlankName() {
        User user = new User(
                "blank",
                "password",
                "   ",
                "blank@email.com",
                "admin"
        );

        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void settersShouldUpdateFields() {
        User user = new User();

        user.setUsername("newuser");
        user.setPassword("newpass");
        user.setName("Jane Doe");
        user.setEmail("jane@email.com");
        user.setFirstName("Janet");
        user.setLastName("Updated");
        user.setRole("doctor");

        assertEquals("newuser", user.getUsername());
        assertEquals("newpass", user.getPassword());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@email.com", user.getEmail());
        assertEquals("Janet", user.getFirstName());
        assertEquals("Updated", user.getLastName());
        assertEquals("DOCTOR", user.getRole());
        assertNotNull(user.getLastPasswordChange());
    }

    @Test
    void setNameShouldSplitFirstAndLastName() {
        User user = new User();

        user.setName("Frederick Amoah-Darko");

        assertEquals("Frederick", user.getFirstName());
        assertEquals("Amoah-Darko", user.getLastName());
    }

    @Test
    void setNameShouldHandleSingleName() {
        User user = new User();

        user.setName("Frederick");

        assertEquals("Frederick", user.getFirstName());
        assertEquals("", user.getLastName());
    }

    @Test
    void setNameShouldHandleBlankNameWithoutChangingFirstAndLastName() {
        User user = new User();
        user.setFirstName("OldFirst");
        user.setLastName("OldLast");

        user.setName("   ");

        assertEquals("   ", user.getName());
        assertEquals("OldFirst", user.getFirstName());
        assertEquals("OldLast", user.getLastName());
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
    void setPasswordShouldSetLastPasswordChange() {
        User user = new User();

        assertNull(user.getLastPasswordChange());

        user.setPassword("newPassword");

        assertEquals("newPassword", user.getPassword());
        assertNotNull(user.getLastPasswordChange());
    }


    @Test
    void equalsShouldReturnTrueForSameObject() {
        User user = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");

        assertEquals(user, user);
    }

    @Test
    void equalsShouldReturnTrueForSameUsername() {
        User user1 = new User("john", "pass1", "John One", "one@email.com", "PATIENT");
        User user2 = new User("john", "pass2", "John Two", "two@email.com", "DOCTOR");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentUsername() {
        User user1 = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");
        User user2 = new User("mary", "pass", "Mary Smith", "mary@email.com", "PATIENT");

        assertNotEquals(user1, user2);
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        User user = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");

        assertNotEquals(null, user);
    }

    @Test
    void equalsShouldReturnFalseForDifferentObjectType() {
        User user = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");

        assertNotEquals("john", user);
    }

    @Test
    void toStringShouldContainImportantFields() {
        User user = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");

        String result = user.toString();

        assertTrue(result.contains("john"));
        assertTrue(result.contains("pass"));
        assertTrue(result.contains("John Smith"));
        assertTrue(result.contains("john@email.com"));
        assertTrue(result.contains("PATIENT"));
    }
}