package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.exception.UnauthorizedException;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import edu.secourse.patientportal.service.AuthenticationService;
import edu.secourse.patientportal.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("fred");
        loginRequest.setPassword("plainPassword");

        user = User.builder()
                .id(1)
                .username("fred")
                .password("encodedPassword")
                .email("fred@test.com")
                .firstName("Fred")
                .lastName("Darko")
                .role("patient")
                .build();
    }

    @Test
    void login_shouldThrowUnauthorizedException_whenUserNotFound() {
        when(userRepository.findByUsername("fred")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authenticationService.login(loginRequest));

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldThrowUnauthorizedException_whenPasswordDoesNotMatch() {
        when(userRepository.findByUsername("fred")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authenticationService.login(loginRequest));

        verify(jwtUtil, never()).generateToken(anyString(), anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnLoginResponseAndSaveUser_whenCredentialsAreValid() {
        when(userRepository.findByUsername("fred")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("fred", "PATIENT")).thenReturn("jwt-token-123");

        LoginResponse response = authenticationService.login(loginRequest);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("fred", response.getUsername());
        assertEquals("fred@test.com", response.getEmail());
        assertEquals("Fred", response.getFirstName());
        assertEquals("Darko", response.getLastName());
        assertEquals("PATIENT", response.getRole());
        assertEquals("jwt-token-123", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600L, response.getExpiresIn());
        assertNotNull(response.getLoginAt());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("PATIENT", savedUser.getRole());
        assertNotNull(savedUser.getLastLogin());

        verify(jwtUtil).generateToken("fred", "PATIENT");
    }

    @Test
    void validateToken_shouldDelegateToJwtUtil() {
        when(jwtUtil.validateToken("abc")).thenReturn(true);

        boolean result = authenticationService.validateToken("abc");

        assertTrue(result);
        verify(jwtUtil).validateToken("abc");
    }

    @Test
    void getUsernameFromToken_shouldDelegateToJwtUtil() {
        when(jwtUtil.extractUsername("abc")).thenReturn("fred");

        String result = authenticationService.getUsernameFromToken("abc");

        assertEquals("fred", result);
        verify(jwtUtil).extractUsername("abc");
    }
}