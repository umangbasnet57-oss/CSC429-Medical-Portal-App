package edu.secourse.patientportal.config;

import edu.secourse.patientportal.model.Admin;
import edu.secourse.patientportal.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class responsible for initializing application data at startup.
 *
 * <p>This class defines a {@link CommandLineRunner} bean that runs when the
 * Spring Boot application starts. It ensures that a default administrator
 * account exists in the system.
 *
 * <p><b>Behavior:</b>
 * <ul>
 *     <li>Reads admin credentials from application properties</li>
 *     <li>Checks if the admin user already exists</li>
 *     <li>If not, creates a new admin user</li>
 * </ul>
 *
 * <p><b>Configuration Properties:</b>
 * <pre>
 * app.admin.username=admin
 * app.admin.password=admin123
 * app.admin.email=admin@example.com
 * </pre>
 *
 * <p><b>Security Notes:</b>
 * <ul>
 *     <li>Password is encoded via {@link PasswordEncoder} during user creation</li>
 *     <li>Prevents duplicate admin creation on application restarts</li>
 * </ul>
 */
@Configuration
public class DataInitializer {

    /** Default admin username from application properties. */
    @Value("${app.admin.username}")
    private String adminUsername;

    /** Default admin password from application properties. */
    @Value("${app.admin.password}")
    private String adminPassword;

    /** Default admin email from application properties. */
    @Value("${app.admin.email}")
    private String adminEmail;

    /**
     * Initializes the database with a default admin user.
     *
     * <p>This method runs automatically at application startup and ensures that
     * an administrator account is available for system access.
     *
     * @param userService service used to manage user operations
     * @param passwordEncoder encoder used for password hashing
     * @return {@link CommandLineRunner} that executes initialization logic
     */
    @Bean
    CommandLineRunner initDatabase(UserService userService,
                                   PasswordEncoder passwordEncoder) {

        return args -> {

            /*
             * Prevent duplicate admin creation on every application restart.
             */
            if (!userService.existsByUsername(adminUsername)) {

                Admin admin = new Admin(
                        adminUsername,
                        adminPassword,
                        "System Admin",
                        adminEmail
                );

                /*
                 * Password encoding is handled inside UserService.
                 */
                userService.createUser(admin);

                // Recommended: replace with logger in production
                System.out.println("Default admin user created successfully.");
            }
        };
    }
}