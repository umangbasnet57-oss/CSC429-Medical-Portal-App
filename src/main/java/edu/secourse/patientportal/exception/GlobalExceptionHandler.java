package edu.secourse.patientportal.exception;

import edu.secourse.patientportal.response.MLogixApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<MLogixApiResponse<Object>> handleUnauthorized(UnauthorizedException ex) {
        MLogixApiResponse<Object> response = MLogixApiResponse.error(
                ex.getMessage(),
                ex.getStatusCode()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}
