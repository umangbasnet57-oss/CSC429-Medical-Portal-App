package edu.secourse.patientportal.exceptions;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Integer id){
        super("Could not find patient with id " + id);
    }
}
