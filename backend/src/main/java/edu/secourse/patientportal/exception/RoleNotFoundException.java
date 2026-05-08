package edu.secourse.patientportal.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends APIException{
    /**
     * Constructor for UnauthorizedException
     *
     * @param message the error message
     */
    public RoleNotFoundException(String message) {
        super(message, HttpStatus.FORBIDDEN.value()); // 401
    }

    /**
     * Constructor with message and cause
     *
     * @param message the error message
     * @param cause the cause exception
     */
    public RoleNotFoundException(String message, Throwable cause) {
        super(message, HttpStatus.FORBIDDEN.value(), cause);
    }
}
