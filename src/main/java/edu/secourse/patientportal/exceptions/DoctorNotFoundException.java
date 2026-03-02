package edu.secourse.patientportal.exceptions;

public class DoctorNotFoundException extends RuntimeException{
    public DoctorNotFoundException(Integer id){
        super("Could not find doctor with id " + id);
    }
}
