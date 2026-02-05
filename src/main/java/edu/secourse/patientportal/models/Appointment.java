package edu.secourse.patientportal.models;

import edu.secourse.patientportal.services.UserService;

import java.time.LocalDateTime;

/**
 * Represents a scheduled appointment between a {@link Patient} and {@link Doctor}.
 * <p>
 * This class includes fields for identifying the appointment, tracking the
 * participants, storing the scheduled date and time, and the appointment status.
 * All setter methods perform basic null/validity checks and return boolean flags
 * instead of throwing exceptions, to prevent runtime failure in the UI flow.
 */
public class Appointment {

    private int appointmentId = 0;
    private Patient patient = new Patient();
    private Doctor doctor = new Doctor();
    private LocalDateTime appointmentDateTime = LocalDateTime.MIN;
    private Status status = Status.UNSPECIFIED;
    private UserService userService = new UserService();

    /**
     * Enumeration representing possible appointment states.
     */
    public enum Status {
        ACTIVE,
        CANCELLED,
        UNSPECIFIED
    }

    /**
     * Constructs an Appointment given a patient, doctor, and scheduled time.
     * If any argument is null, the default values are retained.
     *
     * @param patient             the patient associated with the appointment
     * @param doctor              the doctor associated with the appointment
     * @param appointmentDateTime the date and time the appointment occurs
     */
    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {
        try {
            if (patient != null && doctor != null && appointmentDateTime != null) {
                this.patient = patient;
                this.doctor = doctor;
                this.appointmentDateTime = appointmentDateTime;
                this.status = Status.ACTIVE;
            }
        } catch (Exception _) {

        }
    }

    /**
     * Returns the current appointment status.
     *
     * @return the appointment status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Attempts to update the status of the appointment.
     *
     * @param status the new status to assign
     * @return true if the update succeeds, false otherwise
     */
    public boolean setStatus(Status status) {
        boolean success = false;
        try {
            if (status != null) {
                this.status = status;
                success = true;
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Retrieves the unique appointment ID.
     *
     * @return the appointment ID
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Assigns a new appointment ID if it falls within valid integer bounds.
     *
     * @param appointmentId the new ID value
     * @return true if the update succeeds, false otherwise
     */
    public boolean setAppointmentId(int appointmentId) {
        boolean success = false;
        try {
            if (appointmentId > Integer.MIN_VALUE && appointmentId < Integer.MAX_VALUE) {
                this.appointmentId = appointmentId;
                success = true;
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Returns the patient linked to the appointment.
     *
     * @return the appointment's patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Attempts to update the patient associated with this appointment.
     *
     * @param patient the new patient object
     * @return true if updated successfully, false otherwise
     */
    public boolean setPatient(Patient patient) {
        boolean success = false;
        try {
            if (patient != null) {
                this.patient = patient;
                success = true;
            }
        } catch (Exception _) {

        }

        return success;
    }

    /**
     * Returns the doctor linked to the appointment.
     *
     * @return the appointment's doctor
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Attempts to update the doctor associated with the appointment.
     *
     * @param doctor the new doctor object
     * @return true if updated successfully, false otherwise
     */
    public boolean setDoctor(Doctor doctor) {
        boolean success = false;
        try {
            if (doctor != null) {
                this.doctor = doctor;
                success = true;
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Gets the scheduled date and time for this appointment.
     *
     * @return the appointment date and time
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Attempts to update the appointment's date and time.
     *
     * @param appointmentDateTime the new date/time value
     * @return true if updated successfully, false otherwise
     */
    public boolean setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        boolean success = false;
        try {
            if (appointmentDateTime != null) {
                this.appointmentDateTime = appointmentDateTime;
                success = true;
            }
        } catch (Exception _) {

        }
        return success;
    }

    /**
     * Cancels the appointment by setting its status to {@link Status#CANCELLED}.
     */
    public void cancelAppointment() {
        try {
            this.status = Status.CANCELLED;
        } catch (Exception _) {

        }
    }

    /**
     * Returns a readable string representation of the appointment, including
     * ID, patient/doctor usernames, date/time, and status. Handles null objects gracefully.
     *
     * @return a formatted appointment string
     */
    @Override
    public String toString() {
        String patientName = (patient != null) ? patient.getUsername() : "Unknown Patient";
        String doctorName = (doctor != null) ? doctor.getUsername() : "Unknown Doctor";
        String time = (appointmentDateTime != null) ? appointmentDateTime.toString() : "No Date";

        return "Appointment {" +
                "ID=" + appointmentId +
                ", Patient='" + patientName + '\'' +
                ", Doctor='" + doctorName + '\'' +
                ", DateTime=" + time +
                ", Status=" + status +
                '}';
    }
}
