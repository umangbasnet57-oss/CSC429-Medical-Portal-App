package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)class JwtUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findByUsername("fred")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByUsername("fred"));
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        User user = User.builder()
                .username("fred")
                .password("encodedPassword")
                .role("ADMIN")
                .build();

        when(userRepository.findByUsername("fred")).thenReturn(Optional.of(user));

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("fred");

        assertNotNull(userDetails);
        assertEquals("fred", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}