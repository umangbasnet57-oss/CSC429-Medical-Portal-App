package edu.secourse.patientportal.models;

import java.util.Objects;

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
public abstract class User {

    private int accountNumber = 0;
    public static int nextAccountNumber = 1;
    public String username = "";
    private String hashedPassword = "";
    private String name = "";
    private String email = "";
    private String role = "";

    /**
     * Default no-argument constructor.
     * <p>
     * Leaves all user fields uninitialized until explicitly set.
     */
    public User() {

    }

    /**
     * Constructs a User with full identifying information, assigning a unique
     * auto-incremented account number.
     *
     * @param username       the username chosen by the user
     * @param hashedPassword the user's hashed password
     * @param name           the user's real name
     * @param email          the user's email address
     * @param role           the role of the user (patient, doctor, admin)
     */
    public User(String username, String hashedPassword, String name, String email, String role) {
        try {
            this.accountNumber = nextAccountNumber++;
            this.username = username;
            this.hashedPassword = hashedPassword;
            this.name = name;
            this.email = email;
            this.role = role;
        } catch (Exception e_) {

        }
    }

    /**
     * Retrieves the unique account number assigned to this user.
     *
     * @return the user's account number
     */
    public int getAccountNumber() {
        return accountNumber;
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
        } catch (Exception e_) {

        }
    }

    /**
     * Retrieves the user's hashed password.
     *
     * @return the hashed password string
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Updates the user's hashed password.
     *
     * @param hashedPassword the new hashed password string
     */
    public void setHashedPassword(String hashedPassword) {
        try {
            this.hashedPassword = hashedPassword;
        } catch (Exception e_) {

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
     * Updates the user's name.
     *
     * @param name the new name string
     */
    public void setName(String name) {
        try {
            this.name = name;
        } catch (Exception e_) {

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
        } catch (Exception e_) {

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
        } catch (Exception e_) {

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
        } catch (Exception e_) {

        }
        return isEqual;
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



