package edu.secourse.patientportal.services;

import edu.secourse.patientportal.dto.AppointmentDTO;
import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.models.User;
import org.springframework.hateoas.EntityModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(AppointmentDTO appointmentDTO);
    boolean cancelAppointment(Integer appointmentId);
    boolean modifyAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime newDateTime);
    List<EntityModel<Appointment>> getAllAppointments();
    Appointment getOneAppointment(Integer id);
    ArrayList<Appointment> getAppointmentsForUser(User user);
}
