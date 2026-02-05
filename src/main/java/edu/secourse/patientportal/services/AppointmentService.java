package edu.secourse.patientportal.services;

import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Service layer responsible for managing {@link Appointment} objects.
 * <p>
 * This class performs storage, lookup, creation, modification, and cancellation
 * of appointments. All operations are wrapped in try-catch blocks to protect
 * the UI layer from runtime crashes.
 */
public class AppointmentService {

    /** Internal list storing all appointments created in the system. */
    private final ArrayList<Appointment> appointments = new ArrayList<>();

    /** Auto-incrementing ID counter for newly created appointments. */
    private int nextId = 1;

    /**
     * Creates a new appointment if it does not duplicate an existing one.
     * <p>
     * Two appointments are considered duplicates if they share:
     * <ul>
     *     <li>Same patient</li>
     *     <li>Same doctor</li>
     *     <li>Same date/time (down to the minute)</li>
     * </ul>
     *
     * @param appointment the appointment to be added
     * @return true if appointment was successfully created, false otherwise
     */
    public boolean createAppointment(Appointment appointment) {
        boolean success = false;
        try {
            boolean exists = false;

            for (Appointment existingAppointment : appointments) {
                boolean samePatient = existingAppointment.getPatient().equals(appointment.getPatient());
                boolean sameDoctor = existingAppointment.getDoctor().equals(appointment.getDoctor());
                boolean sameTime = existingAppointment.getAppointmentDateTime()
                        .truncatedTo(ChronoUnit.MINUTES)
                        .equals(appointment.getAppointmentDateTime().truncatedTo(ChronoUnit.MINUTES));

                if (samePatient && sameDoctor && sameTime) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                appointment.setAppointmentId(nextId++);
                appointments.add(appointment);
                success = true;
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Cancels an existing appointment by ID by marking its status as CANCELLED.
     *
     * @param appointmentId the ID of the appointment to cancel
     * @return true if cancellation succeeded, false if not found
     */
    public boolean cancelAppointment(int appointmentId) {
        boolean success = false;
        try {
            for (int i = 0; i < appointments.size(); i++) {
                if (appointments.get(i) != null) {
                    Appointment appointment = appointments.get(i);

                    if (appointment.getAppointmentId() == appointmentId) {
                        appointment.setStatus(Appointment.Status.CANCELLED);
                        success = true;
                    }
                }
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Modifies an existing appointment by updating its patient, doctor, and date/time.
     *
     * @param appointmentId the ID of the appointment to modify
     * @param patient       the new patient object
     * @param doctor        the new doctor object
     * @param newDateTime   the new appointment date/time
     * @return true if the appointment was modified, false otherwise
     */
    public boolean modifyAppointment(int appointmentId, Patient patient, Doctor doctor, LocalDateTime newDateTime) {
        boolean success = false;
        try {
            for (Appointment value : appointments) {
                if (value != null) {
                    if (value.getAppointmentId() == appointmentId) {
                        value.setPatient(patient);
                        value.setDoctor(doctor);
                        value.setAppointmentDateTime(newDateTime);
                        value.setStatus(Appointment.Status.ACTIVE);
                        success = true;
                    }
                }
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Retrieves all appointments associated with a specific user.
     * <p>
     * Rules:
     * <ul>
     *     <li>If user is a Patient → return all appointments in which they are the patient</li>
     *     <li>If user is a Doctor → return all appointments in which they are the doctor</li>
     * </ul>
     *
     * @param user the user whose appointments should be returned
     * @return a list of matching appointments (empty if none or if user is invalid)
     */
    public ArrayList<Appointment> getAppointmentsForUser(User user) {
        ArrayList<Appointment> result = new ArrayList<>();
        try {
            if (user != null) {
                for (Appointment appointment : appointments) {
                    if (user instanceof Patient && appointment.getPatient().equals(user)) {
                        result.add(appointment);
                    } else if (user instanceof Doctor && appointment.getDoctor().equals(user)) {
                        result.add(appointment);
                    }
                }
            }
        } catch (Exception _) {

        }
        return result;
    }
}
