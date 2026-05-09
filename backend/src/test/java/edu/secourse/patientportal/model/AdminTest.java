package edu.secourse.patientportal.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void defaultConstructorShouldCreateAdminObject() {
        Admin admin = new Admin();

        assertNotNull(admin);
        assertEquals("", admin.getUsername());
        assertEquals("", admin.getPassword());
        assertEquals("", admin.getName());
        assertEquals("", admin.getEmail());
    }

    @Test
    void constructorShouldSetAdminFields() {
        Admin admin = new Admin(
                "admin1",
                "hashedPassword",
                "Admin User",
                "admin@email.com"
        );

        assertEquals("admin1", admin.getUsername());
        assertEquals("hashedPassword", admin.getPassword());
        assertEquals("Admin User", admin.getName());
        assertEquals("admin@email.com", admin.getEmail());
        assertEquals("ADMIN", admin.getRole());
    }

    @Test
    void constructorShouldSplitFirstAndLastName() {
        Admin admin = new Admin(
                "admin2",
                "pass",
                "Frederick Amoah-Darko",
                "frederick@email.com"
        );

        assertEquals("Frederick", admin.getFirstName());
        assertEquals("Amoah-Darko", admin.getLastName());
    }

    @Test
    void adminShouldUseInheritedSetters() {
        Admin admin = new Admin();

        admin.setUsername("newadmin");
        admin.setPassword("newpass");
        admin.setName("Jane Doe");
        admin.setEmail("jane@email.com");
        admin.setRole("admin");

        assertEquals("newadmin", admin.getUsername());
        assertEquals("newpass", admin.getPassword());
        assertEquals("Jane Doe", admin.getName());
        assertEquals("jane@email.com", admin.getEmail());
        assertEquals("ADMIN", admin.getRole());
        assertEquals("Jane", admin.getFirstName());
        assertEquals("Doe", admin.getLastName());
        assertNotNull(admin.getLastPasswordChange());
    }

    @Test
    void adminShouldBeInstanceOfUser() {
        Admin admin = new Admin();

        assertTrue(admin instanceof User);
    }
}