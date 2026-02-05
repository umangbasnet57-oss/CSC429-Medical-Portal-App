package edu.patientportal.controllers;

import edu.secourse.patientportal.models.*;
import edu.secourse.patientportal.services.AppointmentService;
import edu.secourse.patientportal.controllers.AppointmentController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link AppointmentController}.
 * <p>
 * These tests verify controller behavior including:
 * <ul>
 *   <li>Creating appointments</li>
 *   <li>Canceling appointments</li>
 *   <li>Modifying appointments</li>
 *   <li>Fetching appointments for users</li>
 *   <li>Handling null or invalid input safely</li>
 * </ul>
 * <p>
 * This class does not use mocks or stubs - all tests run against the real
 * {@link AppointmentService} and real model objects.
 */
class AppointmentControllerTest {

    /**
     * Verifies that the controller handles a null {@link AppointmentService}
     * without throwing errors.
     */
    @Test
    @DisplayName("nullContructorTest(): test null constructor")
    void nullConstructorTest() {
        // Arrange
        AppointmentController controller = new AppointmentController(null);

        // Act & Assert
        assertNotNull(controller);
    }

    /**
     * Tests successful creation of an appointment using valid objects.
     */
    @Test
    @DisplayName("createAppointmentTest(): test create appointment")
    void createAppointmentTest() {
        // Arrange
        AppointmentService service = new AppointmentService();
        AppointmentController controller = new AppointmentController(service);
        Patient patient = new Patient("p", "pass", "Pat", "p@mail.com");
        Doctor doctor = new Doctor("d", "pass", "Doc", "d@mail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now().plusDays(1));

        // Act
        boolean result = controller.createAppointment(appointment);

        // Assert
        assertTrue(result);
    }

    /**
     * Ensures the controller returns false when attempting to create a null appointment.
     */
    @Test
    @DisplayName("createFalseAppointmentTest(): test create bad appointment")
    void createFalseAppointmentTest() {
        // Arrange
        AppointmentController controller = new AppointmentController(new AppointmentService());

        // Act
        boolean result = controller.createAppointment(null);

        // Assert
        assertFalse(result);
    }

    /**
     * Verifies that canceling a non-existing appointment returns false.
     */
    @Test
    @DisplayName("cancelFalseAppointmentTest(): cancel false appointment test")
    void cancelFalseAppointmentTest() {
        // Arrange
        AppointmentController controller = new AppointmentController(new AppointmentService());

        // Act
        boolean result = controller.cancelAppointment(999);

        // Assert
        assertFalse(result);
    }

    /**
     * Tests successful cancellation of an appointment that exists.
     */
    @Test
    @DisplayName("cancelAppointmentTest(): cancel appointment test")
    void cancelAppointmentTest() {
        // Arrange
        AppointmentService service = new AppointmentService();
        AppointmentController controller = new AppointmentController(service);
        Patient patient = new Patient("p", "pass", "Pat", "p@mail.com");
        Doctor doctor = new Doctor("d", "pass", "Doc", "d@mail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now().plusDays(1));

        // Act
        service.createAppointment(appointment);
        int id = appointment.getAppointmentId();
        boolean result = controller.cancelAppointment(id);

        // Assert
        assertTrue(result);
    }

    /**
     * Tests modifying an existing appointment with new valid information.
     */
    @Test
    @DisplayName("modifyAppointmentTest(): modify appointment test")
    void modifyAppointmentTest() {
        // Arrange
        AppointmentService service = new AppointmentService();
        AppointmentController controller = new AppointmentController(service);
        Patient patient = new Patient("p", "pass", "Pat", "p@mail.com");
        Doctor doctor = new Doctor("d", "pass", "Doc", "d@mail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now().plusDays(1));

        // Act
        service.createAppointment(appointment);
        int id = appointment.getAppointmentId();
        LocalDateTime newTime = LocalDateTime.now().plusDays(2);
        boolean result = controller.modifyAppointment(id, patient, doctor, newTime);

        // Assert
        assertTrue(result);
    }

    /**
     * Tests that modification fails when any required parameter is null.
     */
    @Test
    @DisplayName("modifyNullAppointmentTest(): modify null appointment test")
    void modifyNullAppointmentTest() {
        // Arrange
        AppointmentController controller = new AppointmentController(new AppointmentService());
        Patient patient = new Patient("p", "pass", "Pat", "p@mail.com");
        Doctor doctor = new Doctor("d", "pass", "Doc", "d@mail.com");

        // Act & Assert
        assertFalse(controller.modifyAppointment(1, null, doctor, LocalDateTime.now()));
        assertFalse(controller.modifyAppointment(1, patient, null, LocalDateTime.now()));
        assertFalse(controller.modifyAppointment(1, patient, doctor, null));
    }

    /**
     * Verifies that modifying an appointment ID that doesn't exist returns false.
     */
    @Test
    @DisplayName("modifyNotFoundAppointmentTest(): modify appointment not found test")
    void modifyNotFoundAppointmentTest() {
        // Arrange
        AppointmentController controller = new AppointmentController(new AppointmentService());
        Patient patient = new Patient("p", "pass", "Pat", "p@mail.com");
        Doctor doctor = new Doctor("d", "pass", "Doc", "d@mail.com");

        // Act
        boolean result = controller.modifyAppointment(999, patient, doctor, LocalDateTime.now().plusHours(1));

        // Assert
        assertFalse(result);
    }

    /**
     * Ensures the controller returns an empty list when null user is provided.
     */
    @Test
    @DisplayName("getNullAppointmentsForUser(): get null appointments for user test")
    void getNullAppointmentsForUser() {
        // Arrange
        AppointmentController controller = new AppointmentController(new AppointmentService());

        // Act
        ArrayList<Appointment> list = controller.getAppointmentsForUser(null);

        // Assert
        assertTrue(list.isEmpty());
    }

    /**
     * Tests retrieval of appointments associated with a specific patient.
     */
    @Test
    @DisplayName("getAppointmentsForUser(): get appointments for user test")
    void getAppointmentsForUser() {
        // Arrange
        AppointmentService service = new AppointmentService();
        AppointmentController controller = new AppointmentController(service);
        Patient patient = new Patient("p", "pass", "Pat", "p@mail.com");
        Doctor doctor = new Doctor("d", "pass", "Doc", "d@mail.com");
        Appointment appt = new Appointment(patient, doctor, LocalDateTime.now().plusDays(1));
        service.createAppointment(appt);

        // Act
        ArrayList<Appointment> result = controller.getAppointmentsForUser(patient);

        // Asssert
        assertEquals(1, result.size());
    }

    /**
     * Ensures printing a null appointment does not throw exceptions.
     */
    @Test
    @DisplayName("printNullAppointmentsTest(): print null appointments test")
    void printNullAppointmentsTest() {
        // Arrange
        AppointmentController controller = new AppointmentController(new AppointmentService());

        // Act
        controller.printAppointment(null);
    }

    /**
     * Tests printing a valid appointment (simply ensures no exceptions).
     */
    @Test
    @DisplayName("printAppointmentsTest(): print appointments test")
    void printAppointmentsTest() {
        // Arrange
        AppointmentController controller = new AppointmentController(new AppointmentService());
        Patient patient = new Patient("p", "pass", "Pat", "p@mail.com");
        Doctor doctor = new Doctor("d", "pass", "Doc", "d@mail.com");
        Appointment appointment = new Appointment(patient, doctor, LocalDateTime.now().plusDays(1));

        // Act
        controller.printAppointment(appointment);
    }
}




