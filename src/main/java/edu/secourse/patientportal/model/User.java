package edu.secourse.patientportal.model;

import jakarta.persistence.*;
//import org.hibernate.annotations.Collate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The base abstract class for all user types in the patient portal system.
 * <p>
 * Mapped to a single {@code users} table using JPA {@link InheritanceType#SINGLE_TABLE}.
 * The {@code role} column acts as the discriminator: Hibernate writes {@code "patient"},
 * {@code "doctor"}, or {@code "admin"} automatically based on the concrete subclass.
 * <p>
 * Per project specs, first and last name are stored separately. Timestamps for
 * last login and last password change are tracked for security requirements.
 */
@Entity
@Data
//@Builder
@SuperBuilder
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Access(AccessType.FIELD)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /** Primary key — assigned by the database after em.persist(). Null = new entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id = null;

//    /** Separate in-memory counter — not linked to the DB primary key. */
//    public static int nextAccountNumber = 1;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    public String username = "";

    @Column(name = "password", nullable = false)
    private String password = "";

    @Column(name = "first_name", length = 100)
    private String firstName = "";

    @Column(name = "last_name", length = 100)
    private String lastName = "";

    @Column(name = "name", length = 200)
    private String name = "";

    @Column(name = "email", unique = true, length = 200)
    private String email = "";

    /**
     * The role field is managed as the JPA discriminator column ("role").
     * It is kept as a @Transient convenience field for in-memory code paths;
     * the actual DB value is written automatically by Hibernate via @DiscriminatorValue.
     */
//    @Transient
//    @Enumerated(EnumType.STRING)
//    private Role role;
    @Column(name = "role", insertable = false, updatable = false)
    private String role = "";

    @Column(name = "last_login")
    private LocalDateTime lastLogin = null;

    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange = null;

//    /**
//     * Default no-argument constructor.
//     * <p>
//     * Leaves all user fields uninitialized until explicitly set.
//     */
//    public User() {
//
//    }

    /**
     * Constructs a User with full identifying information, assigning a unique
     * auto-incremented account number. The {@code name} is automatically split
     * into {@code firstName} (first word) and {@code lastName} (remainder).
     *
     * @param username       the username chosen by the user
     * @param password the user's hashed password
     * @param name           the user's real name
     * @param email          the user's email address
     * @param role           the role of the user (patient, doctor, admin)
     */
    public User(String username, String password, String name, String email, String role) {
        try {
            this.username = username;
            this.password = password;
            this.name = name;
            this.email = email;
            this.role = role.toUpperCase();
//            nextAccountNumber++;
            if (name != null && !name.isBlank()) {
                String[] parts = name.trim().split("\\s+", 2);
                this.firstName = parts[0];
                this.lastName = (parts.length > 1) ? parts[1] : "";
            }
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the unique account number assigned to this user.
     *
     * @return the user's account number
     */
    public int getId() {
        return id != null ? id : 0;
    }

    /**
     * Retrieves the user's username.
     *
     * @return the username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates the user's username.
     *
     * @param username the new username string
     */
    public void setUsername(String username) {
        try {
            this.username = username;
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the user's hashed password.
     *
     * @return the hashed password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the user's hashed password and records the change timestamp.
     *
     * @param hashedPassword the new hashed password string
     */
    public void setPassword(String hashedPassword) {
        try {
            this.password = hashedPassword;
            this.lastPasswordChange = LocalDateTime.now();
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the user's name.
     *
     * @return the user's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the user's full name and automatically splits it into
     * {@code firstName} (first word) and {@code lastName} (remainder).
     *
     * @param name the new name string
     */
    public void setName(String name) {
        try {
            this.name = name;
            if (name != null && !name.isBlank()) {
                String[] parts = name.trim().split("\\s+", 2);
                this.firstName = parts[0];
                this.lastName = (parts.length > 1) ? parts[1] : "";
            }
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the user's email address.
     *
     * @return the user's email string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the user's email address.
     *
     * @param email the new email string
     */
    public void setEmail(String email) {
        try {
            this.email = email;
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the user's first name.
     *
     * @return the first name string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Updates the user's first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        try {
            this.firstName = firstName;
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the user's last name.
     *
     * @return the last name string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Updates the user's last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        try {
            this.lastName = lastName;
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the timestamp of the user's most recent login.
     *
     * @return the last login timestamp, or {@code null} if the user has never logged in
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    /**
     * Records the current time as the user's last login timestamp.
     */
    public void recordLogin() {
        try {
            this.lastLogin = LocalDateTime.now();
        } catch (Exception ignored) {

        }
    }

    /**
     * Retrieves the timestamp of the user's most recent password change.
     *
     * @return the last password change timestamp, or {@code null} if never changed
     */
    public LocalDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    /**
     * Retrieves the user's role.
     *
     * @return the role string
     */
    public String getRole() {
        if (this.role != null) {
            return role.toUpperCase();
        }
        if (this instanceof Patient) return "PATIENT";
        if (this instanceof Doctor) return "DOCTOR";
        if (this instanceof Admin) return "ADMIN";
        return "UNKNOWN";
    }

    /**
     * Updates the user's role.
     *
     * @param role the new role (patient, doctor, admin)
     */
    public void setRole(String role) {
        try {
            this.role = role.toUpperCase();
        } catch (Exception ignored) {

        }
    }

    /**
     * Determines whether this user is equal to another object.
     * <p>
     * Users are considered equal if:
     * <ul>
     *     <li>The other object is also a User</li>
     *     <li>The usernames match</li>
     * </ul>
     * All exceptions are swallowed to avoid UI crashes.
     *
     * @param o the object to compare
     * @return true if both users have the same username, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        try {
            if (o != null) {
                if (this == o) return true;
                if (!(o instanceof User)) return false;
                User user = (User) o;
                return Objects.equals(this.username, user.username);
            }
        } catch (Exception ignored) {

        }
        return isEqual;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", lastLogin=" + lastLogin +
                ", lastPasswordChange=" + lastPasswordChange +
                '}';
    }

    /**
     * Returns a hash code based on the username field.
     *
     * @return the computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}



