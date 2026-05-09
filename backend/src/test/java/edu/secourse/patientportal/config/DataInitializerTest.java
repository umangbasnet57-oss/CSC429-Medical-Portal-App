package edu.secourse.patientportal.config;

import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Test
    void initDatabaseShouldCreateAdminWhenAdminDoesNotExist() throws Exception {
        UserService userService = mock(UserService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        DataInitializer dataInitializer = new DataInitializer();

        ReflectionTestUtils.setField(dataInitializer, "adminUsername", "admin");
        ReflectionTestUtils.setField(dataInitializer, "adminPassword", "password");
        ReflectionTestUtils.setField(dataInitializer, "adminEmail", "admin@email.com");

        when(userService.existsByUsername("admin")).thenReturn(false);

        CommandLineRunner runner =
                dataInitializer.initDatabase(userService, passwordEncoder);

        runner.run();

        verify(userService).existsByUsername("admin");
        verify(userService).createUser(any(Admin.class));
    }

    @Test
    void initDatabaseShouldNotCreateAdminWhenAdminAlreadyExists() throws Exception {
        UserService userService = mock(UserService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        DataInitializer dataInitializer = new DataInitializer();

        ReflectionTestUtils.setField(dataInitializer, "adminUsername", "admin");
        ReflectionTestUtils.setField(dataInitializer, "adminPassword", "password");
        ReflectionTestUtils.setField(dataInitializer, "adminEmail", "admin@email.com");

        when(userService.existsByUsername("admin")).thenReturn(true);

        CommandLineRunner runner =
                dataInitializer.initDatabase(userService, passwordEncoder);

        runner.run();

        verify(userService).existsByUsername("admin");
        verify(userService, never()).createUser(any(Admin.class));
    }
}