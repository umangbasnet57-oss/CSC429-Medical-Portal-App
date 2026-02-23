package edu.secourse.patientportal.exceptions;

public class AdminNotFoundException extends RuntimeException{
    public AdminNotFoundException(Integer id){
        super("Could not find administrator with id " + id);
    }
}
