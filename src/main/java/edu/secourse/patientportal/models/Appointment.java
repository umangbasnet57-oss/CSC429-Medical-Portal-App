package edu.secourse.patientportal.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a scheduled appointment between a {@link User} and {@link User}.
 * <p>
 * This class includes fields for identifying the appointment, tracking the
 * participants, storing the scheduled date and time, and the appointment status.
 * All setter methods perform basic null/validity checks and return boolean flags
 * instead of throwing exceptions, to prevent runtime failure in the UI flow.
 */
@Entity
public class Appointment {

    private @Id
    int appointmentId = 0;

    @OneToOne
    @JoinColumn(name = "patient_user_id")
    private User patient;

    @OneToOne
    @JoinColumn(name = "doctor_user_id")
    private User doctor;

    private LocalDate appointmentDate = LocalDate.MIN;
    private LocalTime startTime = LocalTime.MIN;
    private LocalTime endTime = LocalTime.MIN;
    private LocalDateTime lastUpdated = LocalDateTime.MIN;
    private Status status = Status.UNSPECIFIED;

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }
//    private UserService userService = new UserService();

    /**
     * Enumeration representing possible appointment states.
     */
    public enum Status {
        ACTIVE,
        CANCELLED,
        UNSPECIFIED
    }

    /**
     * Default no-argument constructor.
     * <p>
     * All fields remain at their default values until explicitly set.
     */
    public Appointment() {

    }

    /**
     * Constructs an Appointment given a patient, doctor, and scheduled time.
     * If any argument is null, the default values are retained.
     *
     * @param patient             the patient associated with the appointment
     * @param doctor              the doctor associated with the appointment
     * @param appointmentDate the date and time the appointment occurs
     */
    public Appointment(User patient, User doctor, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
        try {
            if (patient != null && doctor != null && appointmentDate != null) {
                this.patient = patient;
                this.doctor = doctor;
                this.appointmentDate = appointmentDate;
                this.startTime = startTime;
                this.endTime = endTime;
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
    public User getPatient() {
        return patient;
    }

    /**
     * Attempts to update the patient associated with this appointment.
     *
     * @param patient the new patient object
     * @return true if updated successfully, false otherwise
     */
    public boolean hasSetPatient(User patient) {
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
    public User getDoctor() {
        return doctor;
    }

    /**
     * Attempts to update the doctor associated with the appointment.
     *
     * @param doctor the new doctor object
     * @return true if updated successfully, false otherwise
     */
    public boolean hasSetDoctor(User doctor) {
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
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Attempts to update the appointment's date and time.
     *
     * @param appointmentDate the new date/time value
     * @return true if updated successfully, false otherwise
     */
    public boolean setAppointmentDate(LocalDate appointmentDate) {
        boolean success = false;
        try {
            if (appointmentDate != null) {
                this.appointmentDate = appointmentDate;
                success = true;
            }
        } catch (Exception _) {

        }
        return success;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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
        String time = (appointmentDate != null) ? appointmentDate.toString() : "No Date";

        return "Appointment {" +
                "ID=" + appointmentId +
                ", Patient='" + patientName + '\'' +
                ", Doctor='" + doctorName + '\'' +
                ", DateTime=" + time +
                ", Status=" + status +
                '}';
    }
}
