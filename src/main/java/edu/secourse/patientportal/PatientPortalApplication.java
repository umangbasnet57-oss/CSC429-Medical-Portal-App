package edu.secourse.patientportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée principal de l'application Spring Boot.
 * <p>
 * {@link SpringBootApplication} active :
 * <ul>
 *     <li>L'auto-configuration Spring Boot (JPA, Web, DataSource…)</li>
 *     <li>Le scan des composants (@Service, @RestController, @Repository…)</li>
 *     <li>L'import automatique des configurations</li>
 * </ul>
 */
@SpringBootApplication
public class PatientPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientPortalApplication.class, args);
    }
}
