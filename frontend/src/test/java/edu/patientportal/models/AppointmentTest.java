package edu.patientportal.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.models.Doctor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Appointment} model class.
 * <p>
 * This test suite verifies:
 * <ul>
 *     <li>Constructor behavior and default status</li>
 *     <li>Correct operation of all setter and getter methods</li>
 *     <li>Status transitions such as cancellation</li>
 *     <li>Mutation of patient, doctor, and appointment time fields</li>
 * </ul>
 * <p>
 * These tests use real {@link Patient} and {@link Doctor} objects,
 * and they do not use mocks or any external dependencies.
 */
public class AppointmentTest {

    /**
     * Tests that the constructor correctly initializes the Appointment
     * with supplied patient, doctor, date/time, and default ACTIVE status.
     */
    @Test
    @DisplayName("constructorTest: constructor test")
    void constructorTest() {
        // Arrange
        Patient patient = new Patient("pat", "pass", "Patient", "pat@mail.com");
        Doctor doctor = new Doctor("doc", "pass", "Doctor", "doc@mail.com");
        LocalDateTime time = LocalDateTime.now();
        Appointment appointment = new Appointment(patient, doctor, time);

        // Act & Assert
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(time, appointment.getAppointmentDateTime());
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());
    }

    /**
     * Confirms that setAppointmentId successfully updates the ID field.
     */
    @Test
    @DisplayName("setAppointmentIdTest: set appointment id test")
    void setAppointmentIdTest() {
        // Arrange
        Appointment a = new Appointment(
                new Patient("p", "pass", "Patient", "p@mail.com"),
                new Doctor("d", "pass", "Doctor", "d@mail.com"),
                LocalDateTime.now()
        );

        // Act
        a.setAppointmentId(42);

        // Assert
        assertEquals(42, a.getAppointmentId());
    }

    /**
     * Ensures that setPatient correctly replaces the stored patient.
     */
    @Test
    @DisplayName("setPatientTest: set patient test")
    void setPatientTest() {
        // Arrange
        Appointment appointment = new Appointment(
                new Patient("p1", "pass", "Pat1", "p1@mail.com"),
                new Doctor("d1", "pass", "Doc1", "d1@mail.com"),
                LocalDateTime.now()
        );

        Patient newPatient = new Patient("p2", "pass", "Pat2", "p2@mail.com");

        // Act
        appointment.setPatient(newPatient);

        // Assert
        assertEquals(newPatient, appointment.getPatient());
    }

    /**
     * Ensures that setDoctor correctly updates the doctor reference.
     */
    @Test
    @DisplayName("setDoctorTest: set doctor test")
    void setDoctorTest() {
        // Arrange
        Appointment appointment = new Appointment(
                new Patient("p1", "pass", "Pat1", "p1@mail.com"),
                new Doctor("d1", "pass", "Doc1", "d1@mail.com"),
                LocalDateTime.now()
        );

        Doctor newDoctor = new Doctor("d2", "pass", "Doc2", "d2@mail.com");

        // Act
        appointment.setDoctor(newDoctor);

        // Assert
        assertEquals(newDoctor, appointment.getDoctor());
    }

    /**
     * Verifies that the appointment date/time can be updated successfully.
     */
    @Test
    @DisplayName("setAppointmentDateTimeTest: set appointments date time test")
    void setAppointmentDateTimeTest() {
        // Arrange
        Appointment appointment = new Appointment(
                new Patient("p1", "pass", "Pat1", "p1@mail.com"),
                new Doctor("d1", "pass", "Doc1", "d1@mail.com"),
                LocalDateTime.now()
        );

        // Act
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        appointment.setAppointmentDateTime(newTime);

        // Assert
        assertEquals(newTime, appointment.getAppointmentDateTime());
    }

    /**
     * Tests that calling cancelAppointment correctly sets the status to CANCELLED.
     */
    @Test
    @DisplayName("cancelAppointmentTest: cancelAppointment test")
    void cancelAppointmentTest() {
        // Arrange
        Appointment appointment = new Appointment(
                new Patient("p1", "pass", "Pat1", "p1@mail.com"),
                new Doctor("d1", "pass", "Doc1", "d1@mail.com"),
                LocalDateTime.now()
        );

        // Act
        appointment.cancelAppointment();

        // Assert
        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
    }

    /**
     * Confirms that setStatus updates the appointment's status manually.
     */
    @Test
    @DisplayName("setStatusTest: setter updates appointment status")
    void setStatusTest() {
        // Arrange
        Appointment appointment = new Appointment(
                new Patient("p1", "pass", "Pat1", "p1@mail.com"),
                new Doctor("d1", "pass", "Doc1", "d1@mail.com"),
                LocalDateTime.now()
        );

        // Act
        appointment.setStatus(Appointment.Status.CANCELLED);

        // Assert
        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
    }
}


