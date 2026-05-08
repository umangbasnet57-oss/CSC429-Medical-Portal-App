package edu.secourse.patientportal.config;

import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Bean
    CommandLineRunner initDatabase(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin already exists to avoid duplicates on every restart
            if (!userService.existsByUsername(adminUsername)) {

                // Create your Admin object using your existing logic
                Admin admin = new Admin(
                        adminUsername,
                        adminPassword,
                        "System Admin",
                        adminEmail
                );

                userService.createUser(admin); // password is encoded here
                System.out.println("Default admin user created successfully.");
            }
        };
    }
}

