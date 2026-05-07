package edu.secourse.patientportal.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a patient within the patient portal system.
 *
 * <p>This class extends {@link User} and represents end users who receive
 * medical services, manage appointments, and access personal health data.
 *
 * <p><b>Persistence Details:</b>
 * <ul>
 *     <li>Stored in the shared {@code users} table</li>
 *     <li>Uses single-table inheritance with discriminator value {@code "PATIENT"}</li>
 * </ul>
 *
 * <p><b>Inheritance:</b>
 * <ul>
 *     <li>Extends {@link User}</li>
 *     <li>Inherits all base user fields (username, password, email, etc.)</li>
 * </ul>
 *
 * <p><b>Role Behavior:</b>
 * <ul>
 *     <li>Automatically assigned role {@code "PATIENT"}</li>
 *     <li>Used for authorization and access control in secured endpoints</li>
 * </ul>
 */
@Entity
@SuperBuilder
@DiscriminatorValue("PATIENT")
public class Patient extends User {

    /**
     * Default constructor required by JPA.
     *
     * <p>Creates an empty Patient instance. Fields should be set via
     * setters or builder pattern.
     */
    public Patient() {
        super();
    }

    /**
     * Constructs a Patient user with required attributes.
     *
     * <p>This constructor initializes the base {@link User} fields
     * and automatically assigns the role {@code "PATIENT"}.
     *
     * @param username       the patient's username
     * @param hashedPassword the patient's hashed password
     * @param name           the patient's full name
     * @param email          the patient's email address
     */
    public Patient(String username, String hashedPassword, String name, String email) {
        super(username, hashedPassword, name, email, "PATIENT");
    }
}