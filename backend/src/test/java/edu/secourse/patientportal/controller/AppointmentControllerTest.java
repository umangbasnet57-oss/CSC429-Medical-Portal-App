package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.service.AppointmentService;
import edu.secourse.patientportal.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    private AppointmentService appointmentService;
    private UserService userService;
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        appointmentService = mock(AppointmentService.class);
        userService = mock(UserService.class);
        appointmentController = new AppointmentController(appointmentService, userService);
    }

    @Test
    void getAppointmentsForUserShouldReturnAppointments() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment());

        when(userService.getUser("patient1")).thenReturn(patient);
        when(appointmentService.getAppointmentsForUser(patient)).thenReturn(appointments);

        ResponseEntity<?> response = appointmentController.getAppointmentsForUser("patient1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appointments, response.getBody());
    }

    @Test
    void getAppointmentsForUserShouldReturnNotFound() {
        when(userService.getUser("missing")).thenReturn(null);

        ResponseEntity<?> response = appointmentController.getAppointmentsForUser("missing");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createAppointmentShouldReturnOk() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");

        Map<String, String> body = Map.of(
                "patientUsername", "patient1",
                "doctorUsername", "doctor1",
                "dateTime", "2026-05-08T10:00:00"
        );

        when(userService.getUser("patient1")).thenReturn(patient);
        when(userService.getUser("doctor1")).thenReturn(doctor);
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(true);

        ResponseEntity<?> response = appointmentController.createAppointment(body);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void createAppointmentShouldReturnBadRequestWhenPatientMissing() {
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");

        Map<String, String> body = Map.of(
                "patientUsername", "missing",
                "doctorUsername", "doctor1",
                "dateTime", "2026-05-08T10:00:00"
        );

        when(userService.getUser("missing")).thenReturn(null);
        when(userService.getUser("doctor1")).thenReturn(doctor);

        ResponseEntity<?> response = appointmentController.createAppointment(body);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Patient not found"));
    }

    @Test
    void createAppointmentShouldReturnBadRequestWhenDoctorMissing() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");

        Map<String, String> body = Map.of(
                "patientUsername", "patient1",
                "doctorUsername", "missing",
                "dateTime", "2026-05-08T10:00:00"
        );

        when(userService.getUser("patient1")).thenReturn(patient);
        when(userService.getUser("missing")).thenReturn(null);

        ResponseEntity<?> response = appointmentController.createAppointment(body);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Doctor not found"));
    }

    @Test
    void createAppointmentShouldReturnBadRequestWhenDuplicate() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");

        Map<String, String> body = Map.of(
                "patientUsername", "patient1",
                "doctorUsername", "doctor1",
                "dateTime", "2026-05-08T10:00:00"
        );

        when(userService.getUser("patient1")).thenReturn(patient);
        when(userService.getUser("doctor1")).thenReturn(doctor);
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(false);

        ResponseEntity<?> response = appointmentController.createAppointment(body);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("This appointment already exists.", response.getBody());
    }

    @Test
    void cancelAppointmentShouldReturnOk() {
        when(appointmentService.cancelAppointment(1)).thenReturn(true);

        ResponseEntity<?> response = appointmentController.cancelAppointment(1);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void cancelAppointmentShouldReturnNotFound() {
        when(appointmentService.cancelAppointment(99)).thenReturn(false);

        ResponseEntity<?> response = appointmentController.cancelAppointment(99);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void modifyAppointmentShouldReturnOk() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");

        Map<String, String> body = Map.of(
                "patientUsername", "patient1",
                "doctorUsername", "doctor1",
                "dateTime", "2026-05-08T11:00:00"
        );

        when(userService.getUser("patient1")).thenReturn(patient);
        when(userService.getUser("doctor1")).thenReturn(doctor);
        when(appointmentService.modifyAppointment(eq(1), eq(patient), eq(doctor), any(LocalDateTime.class)))
                .thenReturn(true);

        ResponseEntity<?> response = appointmentController.modifyAppointment(1, body);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteAppointmentShouldReturnOk() {
        when(appointmentService.deleteAppointment(1)).thenReturn(true);

        ResponseEntity<?> response = appointmentController.deleteAppointment(1);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteAppointmentShouldReturnNotFound() {
        when(appointmentService.deleteAppointment(99)).thenReturn(false);

        ResponseEntity<?> response = appointmentController.deleteAppointment(99);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void toggleAppointmentStatusShouldReturnOk() {
        when(appointmentService.toggleAppointmentStatus(1)).thenReturn(true);

        ResponseEntity<?> response = appointmentController.toggleAppointmentStatus(1);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void completeAppointmentShouldReturnOk() {
        when(appointmentService.completeAppointment(1)).thenReturn(true);

        ResponseEntity<?> response = appointmentController.completeAppointment(1);

        assertEquals(200, response.getStatusCodeValue());
    }
}