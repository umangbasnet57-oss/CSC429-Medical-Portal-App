package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void getUserShouldReturnUser() {
        User user = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");

        when(userService.getUser("john")).thenReturn(user);

        ResponseEntity<?> response = userController.getUser("john");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserShouldReturnNotFound() {
        when(userService.getUser("missing")).thenReturn(null);

        ResponseEntity<?> response = userController.getUser("missing");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getAllUsersShouldReturnUsers() {
        List<User> users = List.of(
                new User("john", "pass", "John Smith", "john@email.com", "PATIENT")
        );

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<?> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    void createUserShouldCreatePatient() {
        Map<String, String> body = Map.of(
                "username", "patient1",
                "password", "pass",
                "name", "Patient One",
                "email", "patient@email.com",
                "role", "PATIENT"
        );

        when(userService.createUser(any(Patient.class))).thenReturn(true);

        ResponseEntity<?> response = userController.createUser(body);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        verify(userService).createUser(any(Patient.class));
    }

    @Test
    void createUserShouldCreateDoctor() {
        Map<String, String> body = Map.of(
                "username", "doctor1",
                "password", "pass",
                "name", "Doctor One",
                "email", "doctor@email.com",
                "role", "DOCTOR"
        );

        when(userService.createUser(any(Doctor.class))).thenReturn(true);

        ResponseEntity<?> response = userController.createUser(body);

        assertEquals(200, response.getStatusCodeValue());

        verify(userService).createUser(any(Doctor.class));
    }

    @Test
    void createUserShouldCreateAdmin() {
        Map<String, String> body = Map.of(
                "username", "admin1",
                "password", "pass",
                "name", "Admin One",
                "email", "admin@email.com",
                "role", "ADMIN"
        );

        when(userService.createUser(any(Admin.class))).thenReturn(true);

        ResponseEntity<?> response = userController.createUser(body);

        assertEquals(200, response.getStatusCodeValue());

        verify(userService).createUser(any(Admin.class));
    }

    @Test
    void createUserShouldUsePatientAsDefaultRole() {
        Map<String, String> body = Map.of(
                "username", "patientDefault",
                "password", "pass",
                "name", "Default Patient",
                "email", "default@email.com"
        );

        when(userService.createUser(any(Patient.class))).thenReturn(true);

        ResponseEntity<?> response = userController.createUser(body);

        assertEquals(200, response.getStatusCodeValue());

        verify(userService).createUser(any(Patient.class));
    }

    @Test
    void createUserShouldReturnBadRequestForInvalidRole() {
        Map<String, String> body = Map.of(
                "username", "baduser",
                "password", "pass",
                "name", "Bad User",
                "email", "bad@email.com",
                "role", "INVALID"
        );

        ResponseEntity<?> response = userController.createUser(body);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error"));
    }

    @Test
    void createUserShouldReturnBadRequestWhenServiceFails() {
        Map<String, String> body = Map.of(
                "username", "patient1",
                "password", "pass",
                "name", "Patient One",
                "email", "patient@email.com",
                "role", "PATIENT"
        );

        when(userService.createUser(any(Patient.class))).thenReturn(false);

        ResponseEntity<?> response = userController.createUser(body);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Could not create user"));
    }

    @Test
    void updateUserShouldReturnOk() {
        Map<String, String> body = Map.of(
                "email", "new@email.com"
        );

        when(userService.updateUser("john", body)).thenReturn(true);

        ResponseEntity<?> response = userController.updateUser("john", body);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User Updated successfully", response.getBody());
    }

    @Test
    void updateUserShouldReturnBadRequestWhenUserMissing() {
        Map<String, String> body = Map.of(
                "email", "new@email.com"
        );

        when(userService.updateUser("missing", body)).thenReturn(false);

        ResponseEntity<?> response = userController.updateUser("missing", body);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User not found or username already taken.", response.getBody());
    }

    @Test
    void updateUserShouldReturnBadRequestWhenExceptionOccurs() {
        Map<String, String> body = Map.of(
                "email", "new@email.com"
        );

        when(userService.updateUser("john", body))
                .thenThrow(new RuntimeException("Update failed"));

        ResponseEntity<?> response = userController.updateUser("john", body);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error"));
    }

    @Test
    void deleteUserShouldReturnOk() {
        User user = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");

        when(userService.getUser("john")).thenReturn(user);
        when(userService.removeUser(user)).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUser("john");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User deleted : john", response.getBody());
    }

    @Test
    void deleteUserShouldReturnNotFound() {
        when(userService.getUser("missing")).thenReturn(null);

        ResponseEntity<?> response = userController.deleteUser("missing");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteUserShouldReturnServerErrorWhenRemoveFails() {
        User user = new User("john", "pass", "John Smith", "john@email.com", "PATIENT");

        when(userService.getUser("john")).thenReturn(user);
        when(userService.removeUser(user)).thenReturn(false);

        ResponseEntity<?> response = userController.deleteUser("john");

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error during deletion.", response.getBody());
    }
}