package edu.secourse.patientportal.dto;

import lombok.Data;

@Data
public class PatientDTO {
    private Integer id;
    private String name;
    private String username;
    private String hashedPassword;
    private String email;
    private String role;
}
