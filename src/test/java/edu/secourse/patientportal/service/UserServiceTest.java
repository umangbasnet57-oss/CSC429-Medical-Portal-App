package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.RegisterRequest;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.repository.UserRepository;
import edu.secourse.patientportal.exception.InvalidInputException;
import edu.secourse.patientportal.exception.ResourceAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest(
                "fred",
                "fred@test.com",
                "plainPassword",
                "Fred",
                "Darko",
                "PATIENT"
        );

        user = User.builder()
                .id(1)
                .username("fred")
                .email("fred@test.com")
                .password("encodedPassword")
                .firstName("Fred")
                .lastName("Darko")
                .role("PATIENT")
                .build();
    }

    @Test
    void register_shouldThrowInvalidInputException_whenUsernameIsNull() {
        registerRequest.setUsername(null);

        assertThrows(InvalidInputException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowInvalidInputException_whenUsernameIsBlank() {
        registerRequest.setUsername("   ");

        assertThrows(InvalidInputException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowResourceAlreadyExistsException_whenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("fred")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldCreateRegularUserAndSave_whenRoleIsNotAdmin() {
        when(userRepository.existsByUsername("fred")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.register(registerRequest);

        assertNotNull(savedUser);
        assertEquals("fred", savedUser.getUsername());
        assertEquals("fred@test.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("Fred", savedUser.getFirstName());
        assertEquals("Darko", savedUser.getLastName());
        assertEquals("PATIENT", savedUser.getRole());
        assertNotNull(savedUser.getLastPasswordChange());

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldCreateAdminAndSave_whenRoleIsAdmin() {
        registerRequest.setRole("ADMIN");

        when(userRepository.existsByUsername("fred")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.register(registerRequest);

        assertNotNull(savedUser);
        assertInstanceOf(Admin.class, savedUser);
        assertEquals("ADMIN", savedUser.getRole());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertNotNull(savedUser.getLastPasswordChange());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldReturnTrueAndSaveEncodedPassword_whenUserDoesNotExist() {
        User newUser = User.builder()
                .username("kwame")
                .password("raw123")
                .role("patient")
                .build();

        when(userRepository.existsByUsername("kwame")).thenReturn(false);
        when(passwordEncoder.encode("raw123")).thenReturn("hashed123");

        boolean result = userService.createUser(newUser);

        assertTrue(result);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("hashed123", saved.getPassword());
        assertEquals("PATIENT", saved.getRole());
        assertNotNull(saved.getLastPasswordChange());
    }

    @Test
    void createUser_shouldReturnFalse_whenUsernameAlreadyExists() {
        User newUser = User.builder()
                .username("kwame")
                .password("raw123")
                .role("patient")
                .build();

        when(userRepository.existsByUsername("kwame")).thenReturn(true);

        boolean result = userService.createUser(newUser);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUser_shouldReturnUser_whenFoundInRepository() {
        when(userRepository.findByUsername("fred")).thenReturn(Optional.of(user));

        User result = userService.getUser("fred");

        assertNotNull(result);
        assertEquals("fred", result.getUsername());
    }

    @Test
    void getUser_shouldReturnNull_whenNotFoundInRepository() {
        when(userRepository.findByUsername("fred")).thenReturn(Optional.empty());

        User result = userService.getUser("fred");

        assertNull(result);
    }

    @Test
    void getAllUsers_shouldReturnRepositoryUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("fred", users.get(0).getUsername());
    }

    @Test
    void removeUser_shouldDeleteAndReturnTrue_whenUserIsNotNull() {
        boolean result = userService.removeUser(user);

        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    void removeUser_shouldReturnFalse_whenUserIsNull() {
        boolean result = userService.removeUser(null);

        assertFalse(result);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void existsByUsername_shouldDelegateToRepository() {
        when(userRepository.existsByUsername("fred")).thenReturn(true);

        boolean result = userService.existsByUsername("fred");

        assertTrue(result);
        verify(userRepository).existsByUsername("fred");
    }

    @Test
    void updateUser_shouldReturnFalse_whenUserDoesNotExist() {
        when(userRepository.findByUsername("fred")).thenReturn(Optional.empty());

        boolean result = userService.updateUser("fred", new HashMap<>());

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldUpdateFieldsAndSave_whenUserExists() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "freddy");
        body.put("email", "freddy@test.com");
        body.put("password", "newPass");
        body.put("first_name", "Freddy");
        body.put("last_name", "Amoah");
        body.put("role", "doctor");

        when(userRepository.findByUsername("fred")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("hashedNewPass");

        LocalDateTime beforeUpdate = user.getLastPasswordChange();

        boolean result = userService.updateUser("fred", body);

        assertTrue(result);
        assertEquals("freddy", user.getUsername());
        assertEquals("freddy@test.com", user.getEmail());
        assertEquals("hashedNewPass", user.getPassword());
        assertEquals("Freddy", user.getFirstName());
        assertEquals("Amoah", user.getLastName());
        assertEquals("DOCTOR", user.getRole());
        assertNotNull(user.getLastPasswordChange());

        if (beforeUpdate != null) {
            assertFalse(user.getLastPasswordChange().isBefore(beforeUpdate));
        }

        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldNotChangeRole_whenRoleIsInvalid() {
        user.setRole("PATIENT");

        Map<String, String> body = new HashMap<>();
        body.put("role", "manager");

        when(userRepository.findByUsername("fred")).thenReturn(Optional.of(user));

        boolean result = userService.updateUser("fred", body);

        assertTrue(result);
        assertEquals("PATIENT", user.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void containsUser_shouldReturnTrue_whenRepositoryFindsUsername() {
        when(userRepository.existsByUsername("fred")).thenReturn(true);

        boolean result = userService.containsUser(user);

        assertTrue(result);
    }
}