package edu.secourse.patientportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.secourse.patientportal.exception.GlobalExceptionHandler;
import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.service.AppointmentManagementService;
import edu.secourse.patientportal.service.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import edu.secourse.patientportal.service.UserManagementService;
import edu.secourse.patientportal.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AppointmentController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentManagementService appointmentService;

    @MockitoBean
    private UserManagementService userService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAppointmentsForUser_shouldReturn200_whenUserExists() throws Exception {
        Patient patient = new Patient();
        patient.setUsername("john123");

        Doctor doctor = new Doctor();
        doctor.setUsername("jack123");

        Appointment appointment = new Appointment(
                patient,
                doctor,
                LocalDateTime.of(2026, 2, 15, 10, 0)
        );

        ArrayList<Appointment> appointments = new ArrayList<>(List.of(appointment));

        when(userService.getUser("john123")).thenReturn(patient);
        when(appointmentService.getAppointmentsForUser(patient)).thenReturn(appointments);

        mockMvc.perform(get(Constants.APPOINTMENT_ENDPOINT + "/user/john123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAppointmentsForUser_shouldReturn404_whenUserDoesNotExist() throws Exception {
        when(userService.getUser("missingUser")).thenReturn(null);

        mockMvc.perform(get(Constants.APPOINTMENT_ENDPOINT + "/user/missingUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAppointment_shouldReturn200_whenAppointmentCreated() throws Exception {
        Patient patient = new Patient();
        patient.setUsername("john123");

        Doctor doctor = new Doctor();
        doctor.setUsername("jack123");

        Map<String, String> body = Map.of(
                "patientUsername", "john123",
                "doctorUsername", "jack123",
                "dateTime", "2026-02-15T10:00:00"
        );

        when(userService.getUser("john123")).thenReturn(patient);
        when(userService.getUser("jack123")).thenReturn(doctor);
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(true);

        mockMvc.perform(post(Constants.APPOINTMENT_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void createAppointment_shouldReturn400_whenPatientNotFound() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setUsername("jack123");

        Map<String, String> body = Map.of(
                "patientUsername", "wrongPatient",
                "doctorUsername", "jack123",
                "dateTime", "2026-02-15T10:00:00"
        );

        when(userService.getUser("wrongPatient")).thenReturn(null);
        when(userService.getUser("jack123")).thenReturn(doctor);

        mockMvc.perform(post(Constants.APPOINTMENT_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Patient not found : wrongPatient"));
    }

    @Test
    void createAppointment_shouldReturn400_whenDoctorNotFound() throws Exception {
        Patient patient = new Patient();
        patient.setUsername("john123");

        Map<String, String> body = Map.of(
                "patientUsername", "john123",
                "doctorUsername", "wrongDoctor",
                "dateTime", "2026-02-15T10:00:00"
        );

        when(userService.getUser("john123")).thenReturn(patient);
        when(userService.getUser("wrongDoctor")).thenReturn(null);

        mockMvc.perform(post(Constants.APPOINTMENT_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Doctor not found : wrongDoctor"));
    }

    @Test
    void createAppointment_shouldReturn400_whenAppointmentAlreadyExists() throws Exception {
        Patient patient = new Patient();
        patient.setUsername("john123");

        Doctor doctor = new Doctor();
        doctor.setUsername("jack123");

        Map<String, String> body = Map.of(
                "patientUsername", "john123",
                "doctorUsername", "jack123",
                "dateTime", "2026-02-15T10:00:00"
        );

        when(userService.getUser("john123")).thenReturn(patient);
        when(userService.getUser("jack123")).thenReturn(doctor);
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(false);

        mockMvc.perform(post(Constants.APPOINTMENT_ENDPOINT)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This appointment already exists."));
    }

    @Test
    void cancelAppointment_shouldReturn200_whenSuccess() throws Exception {
        when(appointmentService.cancelAppointment(1)).thenReturn(true);

        mockMvc.perform(delete(Constants.APPOINTMENT_ENDPOINT + "/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Appoitment 1 cancelled."));
    }

    @Test
    void cancelAppointment_shouldReturn404_whenAppointmentNotFound() throws Exception {
        when(appointmentService.cancelAppointment(99)).thenReturn(false);

        mockMvc.perform(delete(Constants.APPOINTMENT_ENDPOINT + "/99/cancel"))
                .andExpect(status().isNotFound());
    }

    @Test
    void modifyAppointment_shouldReturn200_whenSuccess() throws Exception {
        Patient patient = new Patient();
        patient.setUsername("john123");

        Doctor doctor = new Doctor();
        doctor.setUsername("jack123");

        Map<String, String> body = Map.of(
                "patientUsername", "john123",
                "doctorUsername", "jack123",
                "dateTime", "2026-02-15T10:00:00"
        );

        when(userService.getUser("john123")).thenReturn(patient);
        when(userService.getUser("jack123")).thenReturn(doctor);
        when(appointmentService.modifyAppointment(
                eq(1),
                any(Patient.class),
                any(Doctor.class),
                any(LocalDateTime.class)
        )).thenReturn(true);

        mockMvc.perform(put(Constants.APPOINTMENT_ENDPOINT + "/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment 1 modified."));
    }

    @Test
    void modifyAppointment_shouldReturn404_whenAppointmentNotFound() throws Exception {
        Patient patient = new Patient();
        patient.setUsername("john123");

        Doctor doctor = new Doctor();
        doctor.setUsername("jack123");

        Map<String, String> body = Map.of(
                "patientUsername", "john123",
                "doctorUsername", "jack123",
                "dateTime", "2026-02-15T10:00:00"
        );

        when(userService.getUser("john123")).thenReturn(patient);
        when(userService.getUser("jack123")).thenReturn(doctor);
        when(appointmentService.modifyAppointment(
                eq(99),
                any(Patient.class),
                any(Doctor.class),
                any(LocalDateTime.class)
        )).thenReturn(false);

        mockMvc.perform(put(Constants.APPOINTMENT_ENDPOINT + "/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void modifyAppointment_shouldReturn400_whenInvalidDateTime() throws Exception {
        Patient patient = new Patient();
        patient.setUsername("john123");

        Doctor doctor = new Doctor();
        doctor.setUsername("jack123");

        Map<String, String> body = Map.of(
                "patientUsername", "john123",
                "doctorUsername", "jack123",
                "dateTime", "not-a-date"
        );

        when(userService.getUser("john123")).thenReturn(patient);
        when(userService.getUser("jack123")).thenReturn(doctor);

        mockMvc.perform(put(Constants.APPOINTMENT_ENDPOINT + "/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}