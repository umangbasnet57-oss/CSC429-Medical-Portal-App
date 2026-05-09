package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.exception.UnauthorizedException;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import edu.secourse.patientportal.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);

        authenticationService =
                new AuthenticationService(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void loginShouldReturnLoginResponse() {

        User user = new User(
                "john",
                "encodedPassword",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("plainPassword");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("plainPassword", "encodedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken("john", "PATIENT"))
                .thenReturn("jwt-token");

        LoginResponse response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("john", response.getUsername());
        assertEquals("jwt-token", response.getToken());
        assertEquals("PATIENT", response.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void loginShouldThrowExceptionWhenUserNotFound() {

        LoginRequest request = new LoginRequest();
        request.setUsername("wrongUser");
        request.setPassword("password");

        when(userRepository.findByUsername("wrongUser"))
                .thenReturn(Optional.empty());

        assertThrows(
                UnauthorizedException.class,
                () -> authenticationService.login(request)
        );
    }

    @Test
    void loginShouldThrowExceptionForInvalidPassword() {

        User user = new User(
                "john",
                "encodedPassword",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("wrongPassword");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(
                UnauthorizedException.class,
                () -> authenticationService.login(request)
        );
    }

    @Test
    void validateTokenShouldReturnTrue() {

        when(jwtUtil.validateToken("token"))
                .thenReturn(true);

        assertTrue(authenticationService.validateToken("token"));
    }

    @Test
    void getUsernameFromTokenShouldReturnUsername() {

        when(jwtUtil.extractUsername("token"))
                .thenReturn("john");

        assertEquals(
                "john",
                authenticationService.getUsernameFromToken("token")
        );
    }
}