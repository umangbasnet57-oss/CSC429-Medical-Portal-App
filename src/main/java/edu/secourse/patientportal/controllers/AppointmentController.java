package edu.secourse.patientportal.controllers;

import edu.secourse.patientportal.models.*;
import edu.secourse.patientportal.services.AppointmentService;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Controller responsible for handling appointment-related operations.
 * <p>
 * This class serves as an intermediary between the user interface layer
 * (CLI/UI) and the {@link AppointmentService}. It performs basic validation,
 * catches exceptions from the service layer, and ensures that invalid input
 * does not crash the program.
 */
public class AppointmentController {

    /**
     * Service used to process and store appointment data.
     */
    private AppointmentService appointmentService = new AppointmentService();

    /**
     * Creates a new controller instance using the provided service.
     *
     * @param appointmentService the service to use; if {@code null}, a default service is used.
     */
    public AppointmentController(AppointmentService appointmentService) {
        try {
            if (appointmentService != null) {
                this.appointmentService = appointmentService;
            }
        } catch (Exception _) {

        }
    }

    /**
     * Attempts to create an appointment using the underlying service.
     *
     * @param appointment the appointment to create; must not be {@code null}.
     * @return {@code true} if creation succeeded, {@code false} otherwise.
     */
    public boolean createAppointment(Appointment appointment) {
        boolean success = false;
        try {
            if (appointment != null) {
                success = appointmentService.createAppointment(appointment);
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Cancels an existing appointment by its ID.
     *
     * @param appointmentId the unique ID of the appointment to cancel.
     * @return {@code true} if cancellation succeeded, {@code false} if no such appointment exists.
     */
    public boolean cancelAppointment(int appointmentId) {
        boolean success = false;
        try {
            success = appointmentService.cancelAppointment(appointmentId);
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Modifies an existing appointment with new patient, doctor, or date values.
     *
     * @param appointmentId the ID of the appointment to modify.
     * @param patient       the updated patient information; must not be {@code null}.
     * @param doctor        the updated doctor information; must not be {@code null}.
     * @param newDateTime   the updated appointment date and time; must not be {@code null}.
     * @return {@code true} if modification succeeded, {@code false} otherwise.
     */
    public boolean modifyAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime newDateTime) {
        boolean success = false;
        try {
            if (patient != null && doctor != null && newDateTime != null) {
                success = appointmentService.modifyAppointment(appointmentId, patient, doctor, newDateTime);
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Retrieves all appointments associated with a specific user.
     * <p>
     * A patient receives all their own appointments; a doctor receives appointments they are assigned to.
     *
     * @param user the user whose appointments are requested.
     * @return a list of appointments for the given user; never {@code null}.
     */
    public ArrayList<Appointment> getAppointmentsForUser(User user) {
        ArrayList<Appointment> userAppointments = new ArrayList<>();
        try {
            if (user != null) {
                userAppointments = appointmentService.getAppointmentsForUser(user);
            }
        } catch (Exception _) {

        }
        return userAppointments;
    }

    /**
     * Prints a textual representation of the appointment using {@code toString()}.
     *
     * @param appointment the appointment to print. If null or invalid, nothing is printed.
     */
    public void printAppointment(Appointment appointment) {
        try {
            System.out.println(appointment.toString());
        } catch (Exception _) {

        }
    }
}

