package edu.secourse.patientportal.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing an administrator within the patient portal system.
 *
 * <p>This class extends {@link User} and represents users with elevated
 * privileges. Admin users are responsible for managing other users,
 * system configuration, and administrative operations.
 *
 * <p><b>Persistence Details:</b>
 * <ul>
 *     <li>Stored in the shared {@code users} table</li>
 *     <li>Uses single-table inheritance with discriminator value {@code "ADMIN"}</li>
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
 *     <li>Automatically assigned role {@code "ADMIN"}</li>
 *     <li>Used for authorization checks in secured endpoints</li>
 * </ul>
 */
@Entity
@SuperBuilder
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    /**
     * Default constructor required by JPA.
     *
     * <p>Initializes an empty Admin instance. Fields should be populated
     * via setters or builder pattern.
     */
    public Admin() {
        super();
    }

    /**
     * Constructs an Admin user with required attributes.
     *
     * <p>This constructor sets the user's role to {@code "ADMIN"} automatically.
     *
     * @param username       the admin's username
     * @param hashedPassword the admin's hashed password
     * @param name           the admin's full name
     * @param email          the admin's email address
     */
    public Admin(String username, String hashedPassword, String name, String email) {
        super(username, hashedPassword, name, email, "ADMIN");
    }
}