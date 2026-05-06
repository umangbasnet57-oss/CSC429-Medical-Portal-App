package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.LoginRequest;
import edu.secourse.patientportal.dto.LoginResponse;
import edu.secourse.patientportal.exception.UnauthorizedException;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private TokenService tokenService;

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
        when(userRepository.findByUsername("fred"))
                .thenReturn(Optional.empty());

        assertThrows(
                UnauthorizedException.class,
                () -> authenticationService.login(loginRequest)
        );

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(tokenService, never()).generateToken(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_shouldThrowUnauthorizedException_whenPasswordDoesNotMatch() {
        when(userRepository.findByUsername("fred"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("plainPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(
                UnauthorizedException.class,
                () -> authenticationService.login(loginRequest)
        );

        verify(tokenService, never()).generateToken(anyString(), anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_shouldReturnLoginResponseAndSaveUser_whenCredentialsAreValid() {
        when(userRepository.findByUsername("fred"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("plainPassword", "encodedPassword"))
                .thenReturn(true);

        when(tokenService.generateToken("fred", "PATIENT"))
                .thenReturn("jwt-token-123");

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

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("PATIENT", savedUser.getRole());
        assertNotNull(savedUser.getLastLogin());

        verify(tokenService).generateToken("fred", "PATIENT");
    }

    @Test
    void validateToken_shouldDelegateToTokenService() {
        when(tokenService.validateToken("abc"))
                .thenReturn(true);

        boolean result = authenticationService.validateToken("abc");

        assertTrue(result);
        verify(tokenService).validateToken("abc");
    }

    @Test
    void getUsernameFromToken_shouldDelegateToTokenService() {
        when(tokenService.extractUsername("abc"))
                .thenReturn("fred");

        String result = authenticationService.getUsernameFromToken("abc");

        assertEquals("fred", result);
        verify(tokenService).extractUsername("abc");
    }
}