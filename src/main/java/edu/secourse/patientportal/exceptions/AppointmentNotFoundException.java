package edu.secourse.patientportal.exceptions;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(Integer id){
        super(("Could not find appointment with id "+ id));
    }
}
