package edu.secourse.patientportal.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user authentication fails
 *
 * HTTP Status: 401 Unauthorized
 *
 * Usage Examples:
 * - Invalid username or password during login
 * - Missing or invalid JWT token
 * - User credentials do not match
 *
 * @author MacLogix Development Team
 */
public class UnauthorizedException extends APIException {

    /**
     * Constructor for UnauthorizedException
     *
     * @param message the error message
     */
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value()); // 401
    }

    /**
     * Constructor with message and cause
     *
     * @param message the error message
     * @param cause the cause exception
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(message, HttpStatus.UNAUTHORIZED.value(), cause);
    }
}
