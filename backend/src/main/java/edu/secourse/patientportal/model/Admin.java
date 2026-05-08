package edu.secourse.patientportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;

/**
 * Represents an administrator user within the patient portal system.
 * <p>
 * Admin extends the abstract {@link User} class and provides an additional
 * auto-incrementing admin-specific identifier. Stored in the shared
 * {@code users} table with {@code role = 'admin'}.
 */
@Entity
@SuperBuilder
@DiscriminatorValue("ADMIN")
public class Admin extends User {

//    @Column(name = "admin_id")
//    private int adminId = 0;
//    private static int nextAdminId = 1;

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
        super(username, hashedPassword, name, email, "ADMIN");

//        this.adminId = nextAdminId;

//        nextAdminId += 1;
    }

    /**
     * Retrieves the unique admin ID associated with this Admin user.
     *
     * @return the admin's ID number
     */
//    public int getAdminId() {
//        return adminId;
//    }
}
