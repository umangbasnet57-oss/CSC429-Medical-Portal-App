package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.*;
import edu.secourse.patientportal.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    // =========================
    // CREATE APPOINTMENT
    // =========================

    @Test
    void createAppointment_shouldReturnTrue_whenNoDuplicate() {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        LocalDateTime time = LocalDateTime.now();

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(time);

        when(appointmentRepository.findByPatient(patient)).thenReturn(List.of());

        boolean result = appointmentService.createAppointment(appointment);

        assertTrue(result);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void createAppointment_shouldReturnFalse_whenDuplicateExists() {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        LocalDateTime time = LocalDateTime.now();

        Appointment existing = new Appointment();
        existing.setPatient(patient);
        existing.setDoctor(doctor);
        existing.setAppointmentDateTime(time);

        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setAppointmentDateTime(time);

        when(appointmentRepository.findByPatient(patient))
                .thenReturn(List.of(existing));

        boolean result = appointmentService.createAppointment(newAppointment);

        assertFalse(result);
        verify(appointmentRepository, never()).save(any());
    }

    // =========================
    // CANCEL APPOINTMENT
    // =========================

    @Test
    void cancelAppointment_shouldReturnTrue_whenFound() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);
        appointment.setStatus(Appointment.Status.ACTIVE);

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));

        boolean result = appointmentService.cancelAppointment(1);

        assertTrue(result);
        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void cancelAppointment_shouldReturnFalse_whenNotFound() {
        when(appointmentRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = appointmentService.cancelAppointment(1);

        assertFalse(result);
        verify(appointmentRepository, never()).save(any());
    }

    // =========================
    // MODIFY APPOINTMENT
    // =========================

    @Test
    void modifyAppointment_shouldReturnTrue_whenFound() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1);

        Patient newPatient = new Patient();
        Doctor newDoctor = new Doctor();
        LocalDateTime newTime = LocalDateTime.now();

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointment));

        boolean result = appointmentService.modifyAppointment(1, newPatient, newDoctor, newTime);

        assertTrue(result);
        assertEquals(newPatient, appointment.getPatient());
        assertEquals(newDoctor, appointment.getDoctor());
        assertEquals(newTime, appointment.getAppointmentDateTime());
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());

        verify(appointmentRepository).save(appointment);
    }

    @Test
    void modifyAppointment_shouldReturnFalse_whenNotFound() {
        when(appointmentRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = appointmentService.modifyAppointment(1, new Patient(), new Doctor(), LocalDateTime.now());

        assertFalse(result);
        verify(appointmentRepository, never()).save(any());
    }

    // =========================
    // GET APPOINTMENTS FOR USER
    // =========================

    @Test
    void getAppointmentsForUser_shouldReturnPatientAppointments() {
        Patient patient = new Patient();

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);

        when(appointmentRepository.findByPatient(patient))
                .thenReturn(List.of(appointment));

        var result = appointmentService.getAppointmentsForUser(patient);

        assertEquals(1, result.size());
    }

    @Test
    void getAppointmentsForUser_shouldReturnDoctorAppointments() {
        Doctor doctor = new Doctor();

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);

        when(appointmentRepository.findByDoctor(doctor))
                .thenReturn(List.of(appointment));

        var result = appointmentService.getAppointmentsForUser(doctor);

        assertEquals(1, result.size());
    }

    @Test
    void getAppointmentsForUser_shouldReturnEmpty_whenUserIsNull() {
        var result = appointmentService.getAppointmentsForUser(null);

        assertTrue(result.isEmpty());
    }
}