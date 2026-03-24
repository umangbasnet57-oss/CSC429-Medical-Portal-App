package edu.secourse.patientportal.controller;

import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.service.AppointmentService;
import edu.secourse.patientportal.service.UserService;
import edu.secourse.patientportal.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * REST controller for appointment management.
 * Base URL: /api/appointments
 */
@RestController
@RequestMapping(Constants.APPOINTMENT_ENDPOINT)
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    /**
     * GET /maclogixapi/v1/appointments/user/{username}
     * Returns all appointments for a user (patient or doctor).
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ResponseEntity<?> getAppointmentsForUser(@PathVariable String username) {
        var user = userService.getUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        ArrayList<Appointment> appointments = appointmentService.getAppointmentsForUser(user);
        return ResponseEntity.ok(appointments);
    }

    /**
     * POST maclogixapi/v1/appointments
     * Create new appointment.
     * Body JSON :
     * {
     *   "patientUsername": "john123",
     *   "doctorUsername":  "jack123",
     *   "dateTime":        "2025-12-12T08:30:00"
     * }
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, String> body) {
        try {
            String patientUsername = body.get("patientUsername");
            String doctorUsername  = body.get("doctorUsername");
            String dateTimeStr     = body.get("dateTime");
//            LocalDateTime dt = LocalDateTime.now();
            var patientUser = userService.getUser(patientUsername);
            var doctorUser  = userService.getUser(doctorUsername);

            if (!(patientUser instanceof Patient)) {
                return ResponseEntity.badRequest().body("Patient not found : " + patientUsername);
            }
            if (!(doctorUser instanceof Doctor)) {
                return ResponseEntity.badRequest().body("Doctor not found : " + doctorUsername);
            }

            LocalDateTime dt = LocalDateTime.parse(dateTimeStr);
            Appointment appointment = new Appointment((Patient) patientUser, (Doctor) doctorUser, dt);
            boolean success = appointmentService.createAppointment(appointment);

            if (success) {
                return ResponseEntity.ok(appointment);
            } else {
                return ResponseEntity.badRequest().body("This appointment already exists.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error : " + e.getMessage());
        }
    }


    /**
     * PUT /maclogixapi/appointments/{id}/cancel
     * Cancel an appointment using the ID.
     */
    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {
        boolean success = appointmentService.cancelAppointment(id);
        if (success) {
            return ResponseEntity.ok("Appoitment " + id + " cancelled.");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /maclogixapi/appointments/{id}
     * Modify an existing appointment.
     * Body JSON :
     * {
     *   "patientUsername": "john123",
     *   "doctorUsername":  "jack123",
     *   "dateTime":        "2026-02-15T10:00:00"
     * }
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modifyAppointment(@PathVariable int id, @RequestBody Map<String, String> body) {
        try {
            String patientUsername = body.get("patientUsername");
            String doctorUsername  = body.get("doctorUsername");
            String dateTimeStr     = body.get("dateTime");

            var patientUser = userService.getUser(patientUsername);
            var doctorUser  = userService.getUser(doctorUsername);

            if (!(patientUser instanceof Patient)) {
                return ResponseEntity.badRequest().body("Patient not found : " + patientUsername);
            }
            if (!(doctorUser instanceof Doctor)) {
                return ResponseEntity.badRequest().body("Doctor not found : " + doctorUsername);
            }

            LocalDateTime dt = LocalDateTime.parse(dateTimeStr);
            boolean success = appointmentService.modifyAppointment(id, (Patient) patientUser, (Doctor) doctorUser, dt);

            if (success) {
                return ResponseEntity.ok("Appointment " + id + " modified.");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error : " + e.getMessage());
        }
    }
}
