package edu.secourse.patientportal.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends APIException{
    public ResourceAlreadyExistsException(String msg){
        super(msg, HttpStatus.CONFLICT.value());
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, HttpStatus.CONFLICT.value(), cause);
    }
}
