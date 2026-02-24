package edu.secourse.patientportal.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer patientId;
    private Integer doctorId;
    private LocalDateTime lastUpdated;
}
