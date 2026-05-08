package edu.secourse.patientportal.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;

/**
 * Represents a patient within the patient portal system.
 * <p>
 * The {@code Patient} entity extends the {@link User} entity and inherits
 * all common user attributes such as username, password, name, email,
 * and role information.
 * </p>
 *
 * <p>
 * This entity participates in JPA inheritance mapping and is stored
 * in the shared {@code users} table using the discriminator value
 * {@code PATIENT}.
 * </p>
 *
 * <p>
 * Patients are users who can interact with the system by managing
 * appointments, viewing medical-related information, and accessing
 * patient-specific portal features.
 * </p>
 *
 * @author Frederick Amoah-Darko
 * @version 1.0
 * @since 1.0
 */
@Entity
@SuperBuilder
@DiscriminatorValue("PATIENT")
public class Patient extends User {

    /**
     * Default no-argument constructor required by JPA.
     * <p>
     * This constructor initializes a patient instance with default values.
     * It is primarily used internally by the persistence framework.
     * </p>
     */
    public Patient() {
        super();
    }

    /**
     * Constructs a new {@code Patient} with the specified account information.
     * <p>
     * The patient role is automatically assigned as {@code PATIENT}.
     * </p>
     *
     * @param username the unique username used for authentication
     * @param hashedPassword the securely hashed password for the account
     * @param name the full name of the patient
     * @param email the patient's email address
     */
    public Patient(String username,
                   String hashedPassword,
                   String name,
                   String email) {

        super(username, hashedPassword, name, email, "PATIENT");
    }
}