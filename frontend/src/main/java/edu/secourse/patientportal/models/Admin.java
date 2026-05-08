package edu.secourse.patientportal.models;

/**
 * Represents an administrator user within the patient portal system.
 * <p>
 * Admin extends the abstract {@link User} class and provides an additional
 * auto-incrementing admin-specific identifier. The ID will only increment
 * if safe integer boundary checks pass.
 */
public class Admin extends User {

    private int adminId = 0;
    private static int nextAdminId = 1;

    /**
     * Default no-argument constructor.
     * <p>
     * Leaves all fields at default values until explicitly set.
     */
    public Admin() {

    }

    /**
     * Constructs an Admin user with full user information and assigns
     * a unique admin ID if integer-boundary conditions allow it.
     *
     * @param username       the admin's username
     * @param hashedPassword the admin's hashed password
     * @param name           the admin's full name
     * @param email          the admin's email address
     */
    public Admin(String username, String hashedPassword, String name, String email) {
        super(username, hashedPassword, name, email, "admin");

        this.adminId = nextAdminId;

        nextAdminId += 1;
    }

    /**
     * Retrieves the unique admin ID associated with this Admin user.
     *
     * @return the admin's ID number
     */
    public int getAdminId() {
        return adminId;
    }
}
