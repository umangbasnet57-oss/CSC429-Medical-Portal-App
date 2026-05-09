package edu.secourse.patientportal.service;

import edu.secourse.patientportal.dto.RegisterRequest;
import edu.secourse.patientportal.exception.InvalidInputException;
import edu.secourse.patientportal.exception.ResourceAlreadyExistsException;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {

        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService =
                new UserService(userRepository, passwordEncoder);
    }

    @Test
    void registerShouldCreateUser() {

        RegisterRequest request = new RegisterRequest();

        request.setUsername("john");
        request.setPassword("password");
        request.setEmail("john@email.com");
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setRole("PATIENT");

        when(userRepository.existsByUsername("john"))
                .thenReturn(false);

        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        User user = userService.register(request);

        assertNotNull(user);
        assertEquals("john", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void registerShouldThrowExceptionForMissingUsername() {

        RegisterRequest request = new RegisterRequest();

        request.setUsername("");

        assertThrows(
                InvalidInputException.class,
                () -> userService.register(request)
        );
    }

    @Test
    void registerShouldThrowExceptionWhenUsernameExists() {

        RegisterRequest request = new RegisterRequest();

        request.setUsername("john");

        when(userRepository.existsByUsername("john"))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.register(request)
        );
    }

    @Test
    void createUserShouldReturnTrue() {

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        when(userRepository.existsByUsername("john"))
                .thenReturn(false);

        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        boolean result = userService.createUser(user);

        assertTrue(result);

        verify(userRepository).save(user);
    }

    @Test
    void createUserShouldReturnFalseWhenUserExists() {

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        when(userRepository.existsByUsername("john"))
                .thenReturn(true);

        boolean result = userService.createUser(user);

        assertFalse(result);
    }

    @Test
    void getUserShouldReturnUser() {

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        User result = userService.getUser("john");

        assertEquals(user, result);
    }

    @Test
    void getAllUsersShouldReturnUsers() {

        List<User> users = new ArrayList<>();

        users.add(new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        ));

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
    }

    @Test
    void removeUserShouldReturnTrue() {

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        boolean result = userService.removeUser(user);

        assertTrue(result);

        verify(userRepository).delete(user);
    }

    @Test
    void existsByUsernameShouldReturnTrue() {

        when(userRepository.existsByUsername("john"))
                .thenReturn(true);

        assertTrue(userService.existsByUsername("john"));
    }

    @Test
    void updateUserShouldReturnTrue() {

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        Map<String, String> body = new HashMap<>();

        body.put("email", "new@email.com");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        boolean result =
                userService.updateUser("john", body);

        assertTrue(result);

        assertEquals("new@email.com", user.getEmail());

        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldReturnFalseIfUserMissing() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        boolean result =
                userService.updateUser("missing", new HashMap<>());

        assertFalse(result);
    }

    @Test
    void containsUserShouldReturnTrue() {

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        when(userRepository.existsByUsername("john"))
                .thenReturn(true);

        assertTrue(userService.containsUser(user));
    }

    @Test
    void registerShouldCreateAdminUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("admin1");
        request.setPassword("password");
        request.setEmail("admin@email.com");
        request.setFirstName("Admin");
        request.setLastName("User");
        request.setRole("ADMIN");

        when(userRepository.existsByUsername("admin1")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.register(request);

        assertNotNull(user);
        assertEquals("admin1", user.getUsername());
        assertEquals("admin@email.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void registerShouldThrowExceptionWhenUsernameIsNull() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(null);

        assertThrows(
                InvalidInputException.class,
                () -> userService.register(request)
        );
    }

    @Test
    void updateUserShouldUpdateAllAllowedFields() {
        User user = new User(
                "john",
                "oldPassword",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        Map<String, String> body = new HashMap<>();
        body.put("username", "johnny");
        body.put("email", "johnny@email.com");
        body.put("password", "newPassword");
        body.put("first_name", "Johnny");
        body.put("last_name", "Updated");
        body.put("name", "Johnny Updated");
        body.put("role", "doctor");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        boolean result = userService.updateUser("john", body);

        assertTrue(result);
        assertEquals("johnny", user.getUsername());
        assertEquals("johnny@email.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("Johnny", user.getFirstName());
        assertEquals("Updated", user.getLastName());
        assertEquals("Johnny Updated", user.getName());
        assertEquals("DOCTOR", user.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void updateUserShouldIgnoreInvalidRole() {
        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        Map<String, String> body = new HashMap<>();
        body.put("role", "manager");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        boolean result = userService.updateUser("john", body);

        assertTrue(result);
        assertEquals("PATIENT", user.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void removeUserShouldReturnFalseWhenUserIsNull() {
        boolean result = userService.removeUser(null);

        assertFalse(result);
    }

    @Test
    void containsUserShouldReturnFalseWhenUserDoesNotExist() {
        User user = new User(
                "missing",
                "password",
                "Missing User",
                "missing@email.com",
                "PATIENT"
        );

        when(userRepository.existsByUsername("missing")).thenReturn(false);

        boolean result = userService.containsUser(user);

        assertFalse(result);
    }

    @Test
    void printUserShouldPrintExistingUser() {
        UserService service = new UserService();
        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        service.users.add(user);

        assertDoesNotThrow(() -> service.printUser("john"));
    }

    @Test
    void printUserShouldHandleMissingUser() {
        UserService service = new UserService();

        assertDoesNotThrow(() -> service.printUser("missing"));
    }

    @Test
    void createUserShouldUseInMemoryListWhenRepositoryIsNull() {
        UserService service = new UserService();

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        boolean result = service.createUser(user);

        assertTrue(result);
        assertEquals(1, service.users.size());
        assertEquals(user, service.users.get(0));
    }

    @Test
    void createUserShouldRejectDuplicateInMemoryUser() {
        UserService service = new UserService();

        User user1 = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        User user2 = new User(
                "john",
                "differentPassword",
                "John Other",
                "other@email.com",
                "PATIENT"
        );

        assertTrue(service.createUser(user1));
        assertFalse(service.createUser(user2));
        assertEquals(1, service.users.size());
    }

    @Test
    void getUserShouldReturnUserFromInMemoryList() {
        UserService service = new UserService();

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        service.users.add(user);

        User result = service.getUser("john");

        assertEquals(user, result);
    }

    @Test
    void getUserShouldReturnNullWhenInMemoryUserMissing() {
        UserService service = new UserService();

        User result = service.getUser("missing");

        assertNull(result);
    }

    @Test
    void getAllUsersShouldReturnInMemoryUsers() {
        UserService service = new UserService();

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        service.users.add(user);

        List<User> result = service.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    void removeUserShouldRemoveUserFromInMemoryList() {
        UserService service = new UserService();

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        service.users.add(user);

        boolean result = service.removeUser(user);

        assertTrue(result);
        assertTrue(service.users.isEmpty());
    }

    @Test
    void removeUserShouldReturnFalseWhenInMemoryUserDoesNotExist() {
        UserService service = new UserService();

        User user = new User(
                "missing",
                "password",
                "Missing User",
                "missing@email.com",
                "PATIENT"
        );

        boolean result = service.removeUser(user);

        assertFalse(result);
    }

    @Test
    void containsUserShouldReturnTrueForInMemoryUser() {
        UserService service = new UserService();

        User user = new User(
                "john",
                "password",
                "John Smith",
                "john@email.com",
                "PATIENT"
        );

        service.users.add(user);

        boolean result = service.containsUser(user);

        assertTrue(result);
    }

    @Test
    void containsUserShouldReturnFalseForMissingInMemoryUser() {
        UserService service = new UserService();

        User user = new User(
                "missing",
                "password",
                "Missing User",
                "missing@email.com",
                "PATIENT"
        );

        boolean result = service.containsUser(user);

        assertFalse(result);
    }
}