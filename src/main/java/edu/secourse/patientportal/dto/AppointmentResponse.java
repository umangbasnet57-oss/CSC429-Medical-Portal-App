package edu.secourse.patientportal.dto;

import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.User;

import java.time.LocalDateTime;

public record AppointmentResponse(
        String patientUsername,
        String doctorUsername,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    // Static factory method to make mapping easier in controller
    public static AppointmentResponse fromEntity(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getPatient().getUsername(),
                appointment.getDoctor().getUsername(),
                appointment.getAppointmentDateTime(),
                appointment.getEndTime()
        );
    }
}
