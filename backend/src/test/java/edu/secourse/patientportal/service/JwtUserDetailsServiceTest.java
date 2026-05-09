package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUserDetailsServiceTest {

    private UserRepository userRepository;
    private JwtUserDetailsService jwtUserDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);

        jwtUserDetailsService =
                new JwtUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsernameShouldReturnUserDetails() {

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails =
                jwtUserDetailsService.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameShouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByUsername("missing")
        );
    }
}