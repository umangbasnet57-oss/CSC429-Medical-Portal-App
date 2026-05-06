package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface AppointmentManagementService {
    boolean createAppointment(Appointment appointment);
    boolean cancelAppointment(int appointmentId);
    boolean modifyAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime newDateTime);
    ArrayList<Appointment> getAppointmentsForUser(User user);
}
