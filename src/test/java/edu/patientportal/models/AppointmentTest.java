package edu.patientportal.models;

import edu.secourse.patientportal.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import edu.secourse.patientportal.models.Appointment;

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
 * These tests use real {@link User} and {@link User} objects,
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
        User patient = new User("pat", "pass", "Patient", "pat lastname", "pat@mail.com","patient");
        User doctor = new User("doc", "pass", "Doctor", "doc lastname","doc@mail.com", "doctor");
        LocalDateTime time = LocalDateTime.now();
        Appointment appointment = new Appointment(patient, doctor, time);

        // Act & Assert
        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(time, appointment.getAppointmentDate());
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
                new User("p", "pass", "Patient", "pat lastname", "p@mail.com", "patient"),
                new User("d", "pass", "Doctor", "doc lastname", "d@mail.com", "doctor"),
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
                new User("p1", "pass", "Pat1", "Pat1 lastname", "p1@mail.com", "patient"),
                new User("d1", "pass", "Doc1", "Doc1 lastname", "d1@mail.com", "doctor"),
                LocalDateTime.now()
        );

        User newPatient = new User("p2", "pass", "Pat2", "Pat2 lastname", "p2@mail.com", "patient");

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
                new User("p1", "pass", "Pat1", "Pat1 lastname", "p1@mail.com", "patient"),
                new User("d1", "pass", "Doc1", "Doc1 lastname", "d1@mail.com", "doctor"),
                LocalDateTime.now()
        );

        User newDoctor = new User("d2", "pass", "Doc2", "Doc2 lastname", "d2@mail.com", "doctor");

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
                new User("p1", "pass", "Pat1", "Pat1 lastname", "p1@mail.com", "patient"),
                new User("d1", "pass", "Doc1", "Doc1 lastname", "d1@mail.com", "doctor"),
                LocalDateTime.now()
        );

        // Act
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        appointment.setAppointmentDateTime(newTime);

        // Assert
        assertEquals(newTime, appointment.getAppointmentDate());
    }

    /**
     * Tests that calling cancelAppointment correctly sets the status to CANCELLED.
     */
    @Test
    @DisplayName("cancelAppointmentTest: cancelAppointment test")
    void cancelAppointmentTest() {
        // Arrange
        Appointment appointment = new Appointment(
                new User("p1", "pass", "Pat1", "Pat1 lastname", "p1@mail.com", "patient"),
                new User("d1", "pass", "Doc1", "Doc1 lastname", "d1@mail.com", "doctor"),
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
                new User("p1", "pass", "Pat1", "Pat1 lastname", "p1@mail.com", "patient"),
                new User("d1", "pass", "Doc1", "Doc1 lastname", "d1@mail.com", "doctor"),
                LocalDateTime.now()
        );

        // Act
        appointment.setStatus(Appointment.Status.CANCELLED);

        // Assert
        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
    }
}


