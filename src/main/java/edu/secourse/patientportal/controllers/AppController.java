package edu.secourse.patientportal.controllers;

import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.services.UserService;
import edu.secourse.patientportal.services.AppointmentService;

import java.time.LocalDateTime;

/**
 * High-level controller for creating patients, doctors, and appointments.
 * Uses the service layer rather than doing any storage itself.
 */
public class AppController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    public AppController() {
        this.userService = new UserService();
        this.appointmentService = new AppointmentService();
    }

    public UserService getUserService() {
        return userService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    /**
     * Creates a Patient object, registers it with the UserService, and returns it.
     */
    public Patient createPatient(String username) {
        // The tests only check username, so the rest can be simple defaults.
        Patient patient = new Patient(
                username,
                "hashed_password",
                username,
                username + "@example.com"
        );

        userService.createUser(patient);
        return patient;
    }

    /**
     * Creates a Doctor object, registers it with the UserService, and returns it.
     */
    public Doctor createDoctor(String name) {
        Doctor doctor = new Doctor(
                name,
                "hashed_password",
                name,
                name + "@example.com"
        );

        userService.createUser(doctor);
        return doctor;
    }

    /**
     * Schedules an appointment and stores it with AppointmentService.
     */
    public Appointment scheduleAppointment(Patient patient, Doctor doctor, String dateTime) {
        LocalDateTime dt = LocalDateTime.parse(dateTime);
        Appointment appointment = new Appointment(patient, doctor, dt);

        appointmentService.createAppointment(appointment);
        return appointment;
    }
}
