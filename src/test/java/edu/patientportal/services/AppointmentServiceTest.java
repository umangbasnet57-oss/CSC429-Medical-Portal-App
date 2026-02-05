package edu.patientportal.services;

import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.services.AppointmentService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link AppointmentService} class.
 *
 * <p>This suite validates:
 * <ul>
 *     <li>Appointment creation and duplicate-prevention behavior</li>
 *     <li>Canceling existing appointments</li>
 *     <li>Modifying appointment details</li>
 *     <li>Filtering appointments by patient or doctor</li>
 *     <li>Correct status and field updates</li>
 * </ul>
 **/
public class AppointmentServiceTest {

    /**
     * Verifies that a valid appointment can be created successfully.
     */
    @Test
    @DisplayName("createAppointment(): create an appointment")
    void createAppointment() {
        // Arrange & Act
        AppointmentService appointmentsService = new AppointmentService();
        Patient patient = new Patient("alice3", "23lkjsdf03j", "Alice Fiona", "alicefiona3@gmail.com");
        Doctor doctor = new Doctor("Doctor Jones", "sljdfoj23j5l", "Raul Jones", "RaulJones@gmail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now());

        // Assert
        assertTrue(appointmentsService.createAppointment(appointment));
    }

    /**
     * Ensures that duplicate appointments (same patient, doctor, and time)
     * are correctly rejected by the service.
     */
    @Test
    @DisplayName("createAppointmentSameTest(): create an appointment twice should fail second time")
    void createAppointmentSameTest() {
        // Arrange & Act
        AppointmentService appointmentsService = new AppointmentService();
        Patient patient = new Patient("alice3", "23lkjsdf03j", "Alice Fiona", "alicefiona3@gmail.com");
        Doctor doctor = new Doctor("Doctor Jones", "sljdfoj23j5l", "Raul Jones", "RaulJones@gmail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now());

        // Assert
        assertTrue(appointmentsService.createAppointment(appointment));
        assertFalse(appointmentsService.createAppointment(appointment));
    }

    /**
     * Tests that an existing appointment can be canceled successfully.
     */
    @Test
    @DisplayName("cancelAppointment(): cancel an appointment")
    public void cancelAppointment() {
        // Arrange
        AppointmentService appointmentsService = new AppointmentService();
        Patient patient = new Patient("alice3", "23lkjsdf03j", "Alice Fiona", "alicefiona3@gmail.com");
        Doctor doctor = new Doctor("dJones", "sljdfoj23j5l", "Raul Jones", "RaulJones@gmail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now());

        // Act
        appointmentsService.createAppointment(appointment);

        // Assert
        assertTrue(appointmentsService.cancelAppointment(appointment.getAppointmentId()));
    }

    /**
     * Ensures that modifying an appointment updates:
     * <ul>
     *     <li>Patient</li>
     *     <li>Doctor</li>
     *     <li>Appointment date/time</li>
     *     <li>Status back to ACTIVE</li>
     * </ul>
     */
    @Test
    @DisplayName("modifyAppointment(): modify an appointment")
    public void modifyAppointment() {
        // Arrange
        AppointmentService appointmentsService = new AppointmentService();
        Patient patient = new Patient("alice3", "23lkjsdf03j", "Alice Fiona", "alicefiona3@gmail.com");
        Doctor doctor = new Doctor("Doctor Jones", "sljdfoj23j5l", "Raul Jones", "RaulJones@gmail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now());
        Patient patientTwo = new Patient("bob3", "23lsljdlff03j", "Bob Bushay", "bobbybushay@gmail.com");
        Doctor doctorTwo = new Doctor("Doctor Knoxs", "lkjldfslkoaaf", "Johnny Knoxs", "JohnnyKnoxs@gmail.com");
        LocalDateTime dateTimeTwo = LocalDateTime.now();
        Appointment appointmentTwo = new Appointment(patientTwo, doctorTwo, dateTimeTwo);

        // Act
        appointmentsService.createAppointment(appointment);

        assertTrue(appointmentsService.modifyAppointment(
                appointment.getAppointmentId(),
                appointmentTwo.getPatient(),
                appointmentTwo.getDoctor(),
                appointmentTwo.getAppointmentDateTime()));

        // Assert
        assertEquals(appointmentTwo.getPatient(), appointment.getPatient());
        assertEquals(appointmentTwo.getAppointmentDateTime(), appointment.getAppointmentDateTime());
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());
    }

    /**
     * Verifies that appointments can be retrieved based on:
     * <ul>
     *     <li>Patient ownership</li>
     *     <li>Doctor assignment</li>
     * </ul>
     * and that returned lists contain the correct entries.
     */
    @Test
    @DisplayName("getAppointments(): gets appointments for correct user")
    public void getAppointmentsTest() {
        // Arrange
        AppointmentService appointmentsService = new AppointmentService();
        Patient patient = new Patient("alice3", "23lkjsdf03j", "Alice Fiona", "alicefiona3@gmail.com");
        Doctor doctor = new Doctor("Doctor Jones", "sljdfoj23j5l", "Raul Jones", "RaulJones@gmail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now());
        Patient patientTwo = new Patient("bob3", "23lsljdlff03j", "Bob Bushay", "bobbybushay@gmail.com");
        Doctor doctorTwo = new Doctor("Doctor Knoxs", "lkjldfslkoaaf", "Johnny Knoxs", "JohnnyKnoxs@gmail.com");
        Appointment appointmentTwo = new Appointment(patientTwo, doctorTwo, LocalDateTime.now());

        // Act
        appointmentsService.createAppointment(appointment);
        appointmentsService.createAppointment(appointmentTwo);

        // Assert
        assertEquals(1, appointmentsService.getAppointmentsForUser(patient).size());
        assertEquals(1, appointmentsService.getAppointmentsForUser(doctor).size());
        assertTrue(appointmentsService.getAppointmentsForUser(patient).contains(appointment));
        assertTrue(appointmentsService.getAppointmentsForUser(patientTwo).contains(appointmentTwo));
    }
}

