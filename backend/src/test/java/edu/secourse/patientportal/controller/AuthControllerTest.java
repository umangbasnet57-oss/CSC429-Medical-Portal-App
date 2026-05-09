package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.dto.RegisterRequest;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.response.MLogixApiResponse;
import edu.secourse.patientportal.service.AuthenticationService;
import edu.secourse.patientportal.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private UserService userService;
    private AuthenticationService authenticationService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        authenticationService = mock(AuthenticationService.class);
        authController = new AuthController(userService, authenticationService);
    }

    @Test
    void registerShouldReturnCreated() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setPassword("password");
        request.setEmail("john@email.com");
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setRole("PATIENT");

        User user = new User("john", "encoded", "John Smith", "john@email.com", "PATIENT");

        when(userService.register(request)).thenReturn(user);

        ResponseEntity<MLogixApiResponse<User>> response = authController.register(request);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void loginShouldReturnOk() {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("password");

        LoginResponse loginResponse = LoginResponse.builder()
                .id(1)
                .username("john")
                .email("john@email.com")
                .firstName("John")
                .lastName("Smith")
                .role("PATIENT")
                .token("jwt-token")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .loginAt(LocalDateTime.now())
                .build();

        when(authenticationService.login(request)).thenReturn(loginResponse);

        ResponseEntity<MLogixApiResponse<LoginResponse>> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}