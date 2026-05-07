package edu.secourse.patientportal.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a doctor within the patient portal system.
 *
 * <p>This class extends {@link User} and represents users with medical
 * responsibilities such as managing patient appointments and records.
 *
 * <p><b>Persistence Details:</b>
 * <ul>
 *     <li>Stored in the shared {@code users} table</li>
 *     <li>Uses single-table inheritance with discriminator value {@code "DOCTOR"}</li>
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
 *     <li>Automatically assigned role {@code "DOCTOR"}</li>
 *     <li>Used for authorization and access control in secured endpoints</li>
 * </ul>
 */
@Entity
@SuperBuilder
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {

    /**
     * Default constructor required by JPA.
     *
     * <p>Creates an empty Doctor instance. Fields should be set via
     * setters or builder pattern.
     */
    public Doctor() {
        super();
    }

    /**
     * Constructs a Doctor user with required attributes.
     *
     * <p>This constructor initializes the base {@link User} fields
     * and automatically assigns the role {@code "DOCTOR"}.
     *
     * @param username       the doctor's username
     * @param hashedPassword the doctor's hashed password
     * @param name           the doctor's full name
     * @param email          the doctor's email address
     */
    public Doctor(String username, String hashedPassword, String name, String email) {
        super(username, hashedPassword, name, email, "DOCTOR");
    }
}