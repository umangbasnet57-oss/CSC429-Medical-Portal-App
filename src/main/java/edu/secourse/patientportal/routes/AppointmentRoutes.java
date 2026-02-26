package edu.secourse.patientportal.routes;

import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.services.AppointmentService;
import edu.secourse.patientportal.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * REST routes for appointment management.
 * Base URL: /api/appointments
 */
@RestController
@RequestMapping("/api/appointments")
public class AppointmentRoutes {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public AppointmentRoutes(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    /**
     * GET /api/appointments/user/{username}
     * Retourne tous les rendez-vous d'un utilisateur (patient ou médecin).
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAppointmentsForUser(@PathVariable String username) {
        var user = userService.getUser(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        ArrayList<Appointment> appointments = appointmentService.getAppointmentsForUser(user);
        return ResponseEntity.ok(appointments);
    }

    /**
     * POST /api/appointments
     * Crée un nouveau rendez-vous.
     * Body JSON :
     * {
     *   "patientUsername": "john123",
     *   "doctorUsername":  "jack123",
     *   "dateTime":        "2025-12-12T08:30:00"
     * }
     */
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, String> body) {
        try {
            String patientUsername = body.get("patientUsername");
            String doctorUsername  = body.get("doctorUsername");
            String dateTimeStr     = body.get("dateTime");

            var patientUser = userService.getUser(patientUsername);
            var doctorUser  = userService.getUser(doctorUsername);

            if (!(patientUser instanceof Patient)) {
                return ResponseEntity.badRequest().body("Patient introuvable : " + patientUsername);
            }
            if (!(doctorUser instanceof Doctor)) {
                return ResponseEntity.badRequest().body("Médecin introuvable : " + doctorUsername);
            }

            LocalDateTime dt = LocalDateTime.parse(dateTimeStr);
            Appointment appointment = new Appointment((Patient) patientUser, (Doctor) doctorUser, dt);
            boolean success = appointmentService.createAppointment(appointment);

            if (success) {
                return ResponseEntity.ok(appointment);
            } else {
                return ResponseEntity.badRequest().body("Ce rendez-vous existe déjà.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    /**
     * PUT /api/appointments/{id}/cancel
     * Annule un rendez-vous par son ID.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {
        boolean success = appointmentService.cancelAppointment(id);
        if (success) {
            return ResponseEntity.ok("Rendez-vous " + id + " annulé.");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PUT /api/appointments/{id}
     * Modifie un rendez-vous existant.
     * Body JSON :
     * {
     *   "patientUsername": "john123",
     *   "doctorUsername":  "jack123",
     *   "dateTime":        "2025-12-15T10:00:00"
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modifyAppointment(@PathVariable int id, @RequestBody Map<String, String> body) {
        try {
            String patientUsername = body.get("patientUsername");
            String doctorUsername  = body.get("doctorUsername");
            String dateTimeStr     = body.get("dateTime");

            var patientUser = userService.getUser(patientUsername);
            var doctorUser  = userService.getUser(doctorUsername);

            if (!(patientUser instanceof Patient)) {
                return ResponseEntity.badRequest().body("Patient introuvable : " + patientUsername);
            }
            if (!(doctorUser instanceof Doctor)) {
                return ResponseEntity.badRequest().body("Médecin introuvable : " + doctorUsername);
            }

            LocalDateTime dt = LocalDateTime.parse(dateTimeStr);
            boolean success = appointmentService.modifyAppointment(id, (Patient) patientUser, (Doctor) doctorUser, dt);

            if (success) {
                return ResponseEntity.ok("Rendez-vous " + id + " modifié.");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
