package edu.secourse.patientportal.models;

/**
 * Represents a patient user within the patient portal system.
 * <p>
 * This class extends the abstract {@link User} class and introduces
 * a patient-specific auto-incrementing identifier. The ID is assigned
 * only if it passes integer boundary safety checks.
 */
public class Patient extends User {

    private int patientId = 0;
    private static int nextPatientId = 1;

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
        super(username, hashedPassword, name, email, "patient");

        this.patientId = nextPatientId;

        nextPatientId += 1;
    }

    /**
     * Retrieves the auto-assigned patient ID.
     *
     * @return the patient's unique identifier
     */
    public int getPatientId() {
        return patientId;
    }
}


