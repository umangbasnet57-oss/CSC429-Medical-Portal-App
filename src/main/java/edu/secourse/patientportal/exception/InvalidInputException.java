package edu.secourse.patientportal.exception;

import org.springframework.http.HttpStatus;

import java.net.http.HttpRequest;

public class InvalidInputException extends APIException{
    public InvalidInputException(String msg){
        super(msg, HttpStatus.BAD_REQUEST.value());
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST.value(), cause);
    }
}
