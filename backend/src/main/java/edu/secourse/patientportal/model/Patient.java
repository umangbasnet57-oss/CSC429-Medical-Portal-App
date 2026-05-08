package edu.secourse.patientportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;

/**
 * Represents a patient user within the patient portal system.
 * <p>
 * This class extends the abstract {@link User} class and introduces
 * a patient-specific auto-incrementing identifier. Stored in the shared
 * {@code users} table with {@code role = 'patient'}.
 */
@Entity
@SuperBuilder
@DiscriminatorValue("PATIENT")
public class Patient extends User {

//    @Column(name = "patient_id")
//    private int patientId = 0;
//    private static int nextPatientId = 1;

    /**
     * Default no-argument constructor.
     * <p>
     * Leaves patient fields in their default state until explicitly assigned.
     */
    public Patient() {

    }

    /**
     * Constructs a Patient using provided identifying information and assigns
     * a safe auto-incrementing patient ID.
     * <p>
     * Integer overflow checks ensure safe incrementing of {@code nextPatientId}.
     *
     * @param username       the patient's username
     * @param hashedPassword the patient's hashed password
     * @param name           the patient's real name
     * @param email          the patient's email address
     */
    public Patient(String username, String hashedPassword, String name, String email) {
        super(username, hashedPassword, name, email, "PATIENT");

//        this.patientId = nextPatientId;?

//        nextPatientId += 1;
    }

    /**
     * Retrieves the auto-assigned patient ID.
     *
     * @return the patient's unique identifier
     */
//    public int getPatientId() {
//        return patientId;
//    }
}


