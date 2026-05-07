package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.dto.AppointmentResponse;
import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.service.AppointmentManagementService;
import edu.secourse.patientportal.service.UserManagementService;
import edu.secourse.patientportal.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * REST controller responsible for managing appointment-related operations.
 *
 * <p>This controller provides endpoints for:
 * <ul>
 *     <li>Retrieving appointments for a user</li>
 *     <li>Creating new appointments</li>
 *     <li>Modifying existing appointments</li>
 *     <li>Cancelling appointments</li>
 * </ul>
 *
 * <p><b>Base Path:</b> {@code /maclogixapi/v1/appointments}
 *
 * <p><b>Security:</b>
 * <ul>
 *     <li>Admins can perform all operations</li>
 *     <li>Patients and doctors can only view their own appointments</li>
 * </ul>
 *
 * <p><b>Design Notes:</b>
 * <ul>
 *     <li>Delegates business logic to {@link AppointmentManagementService}</li>
 *     <li>Uses {@link UserManagementService} for user validation</li>
 *     <li>Returns DTOs (e.g., {@link AppointmentResponse}) for API safety</li>
 * </ul>
 */
@RestController
@RequestMapping(Constants.APPOINTMENT_ENDPOINT)
public class AppointmentController {

    /** Service responsible for appointment operations. */
    private final AppointmentManagementService appointmentService;

    /** Service responsible for retrieving users. */
    private final UserManagementService userService;

    /**
     * Constructs the AppointmentController.
     *
     * @param appointmentService service for appointment logic
     * @param userService service for user retrieval
     */
    public AppointmentController(AppointmentManagementService appointmentService,
                                 UserManagementService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    /**
     * Retrieves all appointments for a given user.
     *
     * <p><b>Endpoint:</b> {@code GET /maclogixapi/v1/appointments/user/{username}}
     *
     * <p><b>Access Control:</b>
     * <ul>
     *     <li>Admin: can access any user's appointments</li>
     *     <li>Patient/Doctor: can only access their own appointments</li>
     * </ul>
     *
     * @param username username of the target user
     * @return 200 OK with appointments list, or 404 Not Found if user does not exist
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN') or (hasAnyRole('PATIENT','DOCTOR') and #username == authentication.name)")
    public ResponseEntity<?> getAppointmentsForUser(@PathVariable String username) {

        var user = userService.getUser(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        ArrayList<Appointment> appointments = appointmentService.getAppointmentsForUser(user);

        return ResponseEntity.ok(appointments);
    }

    /**
     * Creates a new appointment.
     *
     * <p><b>Endpoint:</b> {@code POST /maclogixapi/v1/appointments}
     *
     * <p><b>Expected Request Body:</b>
     * <pre>
     * {
     *   "patientUsername": "john123",
     *   "doctorUsername":  "jack123",
     *   "dateTime":        "2025-12-12T08:30:00"
     * }
     * </pre>
     *
     * <p><b>Behavior:</b>
     * <ul>
     *     <li>Validates that both patient and doctor exist</li>
     *     <li>Parses the provided date-time</li>
     *     <li>Creates a new appointment if it does not already exist</li>
     * </ul>
     *
     * @param body request body containing appointment details
     * @return 200 OK with created appointment, or 400 Bad Request on failure
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, String> body) {

        try {
            String patientUsername = body.get("patientUsername");
            String doctorUsername  = body.get("doctorUsername");
            String dateTimeStr     = body.get("dateTime");

            var patientUser = userService.getUser(patientUsername);
            var doctorUser  = userService.getUser(doctorUsername);

            if (!(patientUser instanceof Patient)) {
                return ResponseEntity.badRequest().body("Patient not found: " + patientUsername);
            }

            if (!(doctorUser instanceof Doctor)) {
                return ResponseEntity.badRequest().body("Doctor not found: " + doctorUsername);
            }

            LocalDateTime dt = LocalDateTime.parse(dateTimeStr);

            Appointment appointment = new Appointment(
                    (Patient) patientUser,
                    (Doctor) doctorUser,
                    dt
            );

            boolean success = appointmentService.createAppointment(appointment);

            if (success) {
                return ResponseEntity.ok(AppointmentResponse.fromEntity(appointment));
            }

            return ResponseEntity.badRequest().body("Appointment conflict: doctor or patient is already booked at this time.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Cancels an existing appointment.
     *
     * <p><b>Endpoint:</b> {@code DELETE /maclogixapi/v1/appointments/{id}/cancel}
     *
     * @param id appointment ID
     * @return 200 OK if cancelled, or 404 Not Found if appointment does not exist
     */
    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {

        boolean success = appointmentService.cancelAppointment(id);

        if (success) {
            return ResponseEntity.ok("Appointment " + id + " cancelled.");
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Modifies an existing appointment.
     *
     * <p><b>Endpoint:</b> {@code PUT /maclogixapi/v1/appointments/{id}}
     *
     * <p><b>Expected Request Body:</b>
     * <pre>
     * {
     *   "patientUsername": "john123",
     *   "doctorUsername":  "jack123",
     *   "dateTime":        "2026-02-15T10:00:00"
     * }
     * </pre>
     *
     * <p><b>Behavior:</b>
     * <ul>
     *     <li>Validates patient and doctor</li>
     *     <li>Parses date-time</li>
     *     <li>Updates appointment if found</li>
     * </ul>
     *
     * @param id appointment ID
     * @param body request body with updated values
     * @return 200 OK if updated, 400 Bad Request on invalid input, or 404 Not Found if not found
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modifyAppointment(@PathVariable int id,
                                               @RequestBody Map<String, String> body) {

        try {
            String patientUsername = body.get("patientUsername");
            String doctorUsername  = body.get("doctorUsername");
            String dateTimeStr     = body.get("dateTime");

            var patientUser = userService.getUser(patientUsername);
            var doctorUser  = userService.getUser(doctorUsername);

            if (!(patientUser instanceof Patient)) {
                return ResponseEntity.badRequest().body("Patient not found: " + patientUsername);
            }

            if (!(doctorUser instanceof Doctor)) {
                return ResponseEntity.badRequest().body("Doctor not found: " + doctorUsername);
            }

            LocalDateTime dt = LocalDateTime.parse(dateTimeStr);

            boolean success = appointmentService.modifyAppointment(
                    id,
                    (Patient) patientUser,
                    (Doctor) doctorUser,
                    dt
            );

            if (success) {
                return ResponseEntity.ok("Appointment " + id + " modified.");
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}