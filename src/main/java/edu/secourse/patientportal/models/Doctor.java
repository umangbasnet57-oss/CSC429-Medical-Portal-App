package edu.secourse.patientportal.models;

/**
 * Represents a doctor within the patient portal system.
 * <p>
 * This class extends the abstract {@link User} class and adds
 * a unique doctor-specific auto-incrementing identifier.
 * <br>
 * The ID is only incremented when integer boundary safety checks pass.
 */
public class Doctor extends User {

    private int doctorId = 0;
    private static int nextDoctorId = 1;

    /**
     * Default no-argument constructor.
     * <p>
     * All fields remain at their default values until explicitly set.
     */
    public Doctor() {

    }

    /**
     * Creates a doctor user by setting the base user attributes and assigning
     * a unique incremented doctor ID.
     * <p>
     * Integer overflow checks ensure safe incrementing of {@code nextDoctorId}.
     *
     * @param username       the doctor's username
     * @param hashedPassword the doctor's hashed password
     * @param name           the doctor's full name
     * @param email          the doctor's email address
     */
    public Doctor(String username, String hashedPassword, String name, String email) {
        super(username, hashedPassword, name, email, "doctor");

        this.doctorId = nextDoctorId;

        nextDoctorId += 1;
    }

    /**
     * Retrieves the doctor-specific ID.
     *
     * @return the unique doctor ID
     */
    public int getDoctorId() {
        return doctorId;
    }
}

