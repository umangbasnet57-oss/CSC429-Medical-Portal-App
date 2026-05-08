package edu.secourse.patientportal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Represents a scheduled appointment between a {@link Patient} and {@link Doctor}.
 * <p>
 * Mapped to the {@code appointments} table. The patient and doctor fields are
 * foreign-key references to the {@code patients} and {@code doctors} tables
 * respectively, expressed via {@link ManyToOne} associations.
 */
@Entity
@Table(name = "appointments")
public class Appointment {

    /** Primary key — assigned by the database after em.persist(). Null = new entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer appointmentId = null;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient = new Patient();

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor = new Doctor();

    @Column(name = "start_time", nullable = false)
    private LocalDateTime appointmentDateTime = LocalDateTime.MIN;

    @Column(name = "end_time")
    private LocalDateTime endTime = LocalDateTime.MIN;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = null;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private Status status = Status.COMPLETED;

    /**
     * Enumeration representing possible appointment states.
     */
    public enum Status {
        ACTIVE,
        CANCELLED,
        COMPLETED
    }

    /** No-argument constructor required by Hibernate for entity instantiation. */
    public Appointment() {}

    /**
     * Constructs an Appointment given a patient, doctor, and scheduled time.
     * The end time is automatically set to 30 minutes after the start time.
     * If any argument is null, the default values are retained.
     *
     * @param patient             the patient associated with the appointment
     * @param doctor              the doctor associated with the appointment
     * @param appointmentDateTime the date and time the appointment starts
     */
    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime) {
        try {
            if (patient != null && doctor != null && appointmentDateTime != null) {
                this.patient = patient;
                this.doctor = doctor;
                this.appointmentDateTime = appointmentDateTime;
                this.endTime = appointmentDateTime.plusMinutes(30);
                this.lastUpdated = LocalDateTime.now();
                this.status = Status.ACTIVE;
            }
        } catch (Exception ignored) {

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
        } catch (Exception ignored) {

        }
        return success;
    }

    /**
     * Retrieves the unique appointment ID.
     *
     * @return the appointment ID, or 0 if not yet persisted
     */
    public int getAppointmentId() {
        return appointmentId != null ? appointmentId : 0;
    }

    /**
     * Assigns a new appointment ID (used by the in-memory service layer).
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
        } catch (Exception ignored) {

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
     * Attempts to update the patient associated with this appointment
     * and records the modification timestamp.
     *
     * @param patient the new patient object
     * @return true if updated successfully, false otherwise
     */
    public boolean setPatient(Patient patient) {
        boolean success = false;
        try {
            if (patient != null) {
                this.patient = patient;
                this.lastUpdated = LocalDateTime.now();
                success = true;
            }
        } catch (Exception ignored) {

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
     * Attempts to update the doctor associated with the appointment
     * and records the modification timestamp.
     *
     * @param doctor the new doctor object
     * @return true if updated successfully, false otherwise
     */
    public boolean setDoctor(Doctor doctor) {
        boolean success = false;
        try {
            if (doctor != null) {
                this.doctor = doctor;
                this.lastUpdated = LocalDateTime.now();
                success = true;
            }
        } catch (Exception ignored) {

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
     * Attempts to update the appointment's start date/time. Also updates
     * end time to 30 minutes after the new start time and records the
     * modification timestamp.
     *
     * @param appointmentDateTime the new start date/time value
     * @return true if updated successfully, false otherwise
     */
    public boolean setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        boolean success = false;
        try {
            if (appointmentDateTime != null) {
                this.appointmentDateTime = appointmentDateTime;
                this.endTime = appointmentDateTime.plusMinutes(30);
                this.lastUpdated = LocalDateTime.now();
                success = true;
            }
        } catch (Exception ignored) {

        }
        return success;
    }

    /**
     * Returns the end time of this appointment.
     *
     * @return the end time as a {@link LocalDateTime}
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Explicitly sets the end time of the appointment.
     *
     * @param endTime the new end time
     * @return true if updated successfully, false otherwise
     */
    public boolean setEndTime(LocalDateTime endTime) {
        boolean success = false;
        try {
            if (endTime != null) {
                this.endTime = endTime;
                this.lastUpdated = LocalDateTime.now();
                success = true;
            }
        } catch (Exception ignored) {

        }
        return success;
    }

    /**
     * Returns the timestamp of the last modification made to this appointment.
     *
     * @return the last updated timestamp, or {@code null} if never modified
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Cancels the appointment by setting its status to {@link Status#CANCELLED}.
     */
    public void cancelAppointment() {
        try {
            this.status = Status.CANCELLED;
        } catch (Exception ignored) {

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

        String end = (endTime != null && endTime != LocalDateTime.MIN) ? endTime.toString() : "No End Time";

        return "Appointment {" +
                "ID=" + appointmentId +
                ", Patient='" + patientName + '\'' +
                ", Doctor='" + doctorName + '\'' +
                ", Start=" + time +
                ", End=" + end +
                ", Status=" + status +
                '}';
    }
}
