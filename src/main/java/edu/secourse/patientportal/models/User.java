package edu.secourse.patientportal.models;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * The base abstract class for all user types in the patient portal system.
 * <p>
 * This class stores common fields such as username, password, name, email,
 * role, and an auto-incrementing account number. Subclasses include
 * {@link Patient}, {@link Doctor}, and {@link Admin}.
 * <p>
 * All setters and logic blocks are wrapped in try-catch blocks to prevent UI
 * crashes, matching the defensive coding style used across the project.
 */
@Entity
//@MappedSuperclass
public class User {

    private @Id int userId = 0;

    public static int nextUserId = 1;
    public String username = "";
    private String password = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String role = "";
    private LocalDateTime lastLogin;
    private LocalDateTime lastPasswordChange;

    /**
     * Default no-argument constructor.
     * <p>
     * Leaves all user fields uninitialized until explicitly set.
     */
    public User() {}

    /**
     * Constructs a User with full identifying information, assigning a unique
     * auto-incremented account number.
     *
     * @param username       the username chosen by the user
     * @param password the user's hashed password
     * @param firstName           the user's real name
     * @param email          the user's email address
     * @param role           the role of the user (patient, doctor, admin)
     */
    public User(String username, String password, String firstName, String lastName, String email, String role) {
        try {
            this.userId = nextUserId++;
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.role = role;
        } catch (Exception _) {

        }
    }


    /**
     * Retrieves the user's username.
     *
     * @return the username string
     */
    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Updates the user's username.
     *
     * @param username the new username string
     */
    public void setUsername(String username) {
        try {
            this.username = username;
        } catch (Exception _) {

        }
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    /**
     * Updates the user's hashed password.
     *
     * @param password the new hashed password string
     */
    public void setPassword(String password) {
        try {
            this.password = password;
        } catch (Exception _) {

        }
    }


    /**
     * Updates the user's name.
     *
     * @param firstName the new name string
     */
    public void setFirstName(String firstName) {
        try {
            this.firstName = firstName;
        } catch (Exception _) {

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
        } catch (Exception _) {

        }
    }

    /**
     * Retrieves the user's role.
     *
     * @return the role string
     */
    public String getRole() {
        return role;
    }

    /**
     * Updates the user's role.
     *
     * @param role the new role (patient, doctor, admin)
     */
    public void setRole(String role) {
        try {
            this.role = role;
        } catch (Exception _) {

        }
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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
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
        } catch (Exception _) {

        }
        return isEqual;
    }
}



