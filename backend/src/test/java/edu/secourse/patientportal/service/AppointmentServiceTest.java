package edu.secourse.patientportal.service;

import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
import edu.secourse.patientportal.model.User;
import edu.secourse.patientportal.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentTime;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService();

        patient = new Patient(
                "patient1",
                "pass",
                "Patient One",
                "patient@email.com"
        );

        doctor = new Doctor(
                "doctor1",
                "pass",
                "Doctor One",
                "doctor@email.com"
        );

        appointmentTime = LocalDateTime.of(2026, 5, 8, 10, 0);
    }

    @Test
    void createAppointmentShouldAddAppointmentInMemory() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);

        boolean result = appointmentService.createAppointment(appointment);

        assertTrue(result);
        assertEquals(1, appointment.getAppointmentId());
    }

    @Test
    void createAppointmentShouldRejectDuplicateInMemory() {
        Appointment appointment1 = new Appointment(patient, doctor, appointmentTime);
        Appointment appointment2 = new Appointment(patient, doctor, appointmentTime);

        assertTrue(appointmentService.createAppointment(appointment1));
        assertFalse(appointmentService.createAppointment(appointment2));
    }

    @Test
    void createAppointmentShouldAllowDifferentTimeInMemory() {
        Appointment appointment1 = new Appointment(patient, doctor, appointmentTime);
        Appointment appointment2 = new Appointment(patient, doctor, appointmentTime.plusHours(1));

        assertTrue(appointmentService.createAppointment(appointment1));
        assertTrue(appointmentService.createAppointment(appointment2));
    }

    @Test
    void cancelAppointmentShouldCancelExistingAppointmentInMemory() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointmentService.createAppointment(appointment);

        boolean result = appointmentService.cancelAppointment(1);

        assertTrue(result);
        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
    }

    @Test
    void cancelAppointmentShouldReturnFalseForMissingAppointmentInMemory() {
        boolean result = appointmentService.cancelAppointment(99);

        assertFalse(result);
    }

    @Test
    void modifyAppointmentShouldUpdateAppointmentInMemory() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointmentService.createAppointment(appointment);

        Patient newPatient = new Patient(
                "patient2",
                "pass",
                "Patient Two",
                "patient2@email.com"
        );

        Doctor newDoctor = new Doctor(
                "doctor2",
                "pass",
                "Doctor Two",
                "doctor2@email.com"
        );

        LocalDateTime newDateTime = LocalDateTime.of(2026, 6, 1, 14, 30);

        boolean result = appointmentService.modifyAppointment(
                1,
                newPatient,
                newDoctor,
                newDateTime
        );

        assertTrue(result);
        assertEquals(newPatient, appointment.getPatient());
        assertEquals(newDoctor, appointment.getDoctor());
        assertEquals(newDateTime, appointment.getAppointmentDateTime());
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());
    }

    @Test
    void modifyAppointmentShouldReturnFalseForMissingAppointmentInMemory() {
        boolean result = appointmentService.modifyAppointment(
                99,
                patient,
                doctor,
                appointmentTime
        );

        assertFalse(result);
    }

    @Test
    void getAppointmentsForPatientShouldReturnPatientAppointmentsInMemory() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointmentService.createAppointment(appointment);

        ArrayList<Appointment> result = appointmentService.getAppointmentsForUser(patient);

        assertEquals(1, result.size());
        assertEquals(appointment, result.get(0));
    }

    @Test
    void getAppointmentsForDoctorShouldReturnDoctorAppointmentsInMemory() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointmentService.createAppointment(appointment);

        ArrayList<Appointment> result = appointmentService.getAppointmentsForUser(doctor);

        assertEquals(1, result.size());
        assertEquals(appointment, result.get(0));
    }

    @Test
    void getAppointmentsForNullUserShouldReturnEmptyList() {
        ArrayList<Appointment> result = appointmentService.getAppointmentsForUser(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAppointmentsForAdminOrRegularUserShouldReturnEmptyList() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointmentService.createAppointment(appointment);

        User regularUser = new User(
                "user1",
                "pass",
                "Regular User",
                "user@email.com",
                "ADMIN"
        );

        ArrayList<Appointment> result = appointmentService.getAppointmentsForUser(regularUser);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteAppointmentShouldRemoveAppointmentInMemory() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointmentService.createAppointment(appointment);

        boolean result = appointmentService.deleteAppointment(1);

        assertTrue(result);
        assertTrue(appointmentService.getAppointmentsForUser(patient).isEmpty());
    }

    @Test
    void deleteAppointmentShouldReturnFalseForMissingAppointmentInMemory() {
        boolean result = appointmentService.deleteAppointment(99);

        assertFalse(result);
    }

    @Test
    void completeAppointmentShouldSetStatusToCompletedInMemory() {
        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointmentService.createAppointment(appointment);
        appointment.cancelAppointment();

        boolean result = appointmentService.completeAppointment(1);

        assertTrue(result);
        assertEquals(Appointment.Status.COMPLETED, appointment.getStatus());
    }

    @Test
    void completeAppointmentShouldReturnFalseForMissingAppointmentInMemory() {
        boolean result = appointmentService.completeAppointment(99);

        assertFalse(result);
    }

    @Test
    void createAppointmentShouldSaveAppointmentWithRepository() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment appointment = new Appointment(patient, doctor, appointmentTime);

        when(repository.findByPatient(patient)).thenReturn(List.of());

        boolean result = appointmentService.createAppointment(appointment);

        assertTrue(result);
        verify(repository).save(appointment);
    }

    @Test
    void createAppointmentShouldRejectDuplicateWithRepository() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment existingAppointment = new Appointment(patient, doctor, appointmentTime);
        Appointment newAppointment = new Appointment(patient, doctor, appointmentTime);

        when(repository.findByPatient(patient))
                .thenReturn(List.of(existingAppointment));

        boolean result = appointmentService.createAppointment(newAppointment);

        assertFalse(result);
        verify(repository, never()).save(newAppointment);
    }

    @Test
    void cancelAppointmentShouldCancelWithRepository() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment appointment = new Appointment(patient, doctor, appointmentTime);

        when(repository.findById(1)).thenReturn(Optional.of(appointment));

        boolean result = appointmentService.cancelAppointment(1);

        assertTrue(result);
        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
        verify(repository).save(appointment);
    }

    @Test
    void cancelAppointmentShouldReturnFalseWhenRepositoryAppointmentMissing() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        when(repository.findById(99)).thenReturn(Optional.empty());

        boolean result = appointmentService.cancelAppointment(99);

        assertFalse(result);
        verify(repository, never()).save(any(Appointment.class));
    }

    @Test
    void modifyAppointmentShouldUpdateWithRepository() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment appointment = new Appointment(patient, doctor, appointmentTime);

        Patient newPatient = new Patient(
                "patient2",
                "pass",
                "Patient Two",
                "patient2@email.com"
        );

        Doctor newDoctor = new Doctor(
                "doctor2",
                "pass",
                "Doctor Two",
                "doctor2@email.com"
        );

        LocalDateTime newDateTime = LocalDateTime.of(2026, 7, 1, 9, 0);

        when(repository.findById(1)).thenReturn(Optional.of(appointment));

        boolean result = appointmentService.modifyAppointment(
                1,
                newPatient,
                newDoctor,
                newDateTime
        );

        assertTrue(result);
        assertEquals(newPatient, appointment.getPatient());
        assertEquals(newDoctor, appointment.getDoctor());
        assertEquals(newDateTime, appointment.getAppointmentDateTime());
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());
        verify(repository).save(appointment);
    }

    @Test
    void modifyAppointmentShouldReturnFalseWhenRepositoryAppointmentMissing() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        when(repository.findById(99)).thenReturn(Optional.empty());

        boolean result = appointmentService.modifyAppointment(
                99,
                patient,
                doctor,
                appointmentTime
        );

        assertFalse(result);
        verify(repository, never()).save(any(Appointment.class));
    }

    @Test
    void getAppointmentsForPatientShouldUseRepository() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment appointment = new Appointment(patient, doctor, appointmentTime);

        when(repository.findByPatient(patient))
                .thenReturn(List.of(appointment));

        ArrayList<Appointment> result =
                appointmentService.getAppointmentsForUser(patient);

        assertEquals(1, result.size());
        assertEquals(appointment, result.get(0));
    }

    @Test
    void getAppointmentsForDoctorShouldUseRepository() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment appointment = new Appointment(patient, doctor, appointmentTime);

        when(repository.findByDoctor(doctor))
                .thenReturn(List.of(appointment));

        ArrayList<Appointment> result =
                appointmentService.getAppointmentsForUser(doctor);

        assertEquals(1, result.size());
        assertEquals(appointment, result.get(0));
    }

    @Test
    void deleteAppointmentShouldDeleteWithRepository() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        when(repository.existsById(1)).thenReturn(true);

        boolean result = appointmentService.deleteAppointment(1);

        assertTrue(result);
        verify(repository).deleteById(1);
    }

    @Test
    void deleteAppointmentShouldReturnFalseWhenRepositoryAppointmentMissing() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        when(repository.existsById(99)).thenReturn(false);

        boolean result = appointmentService.deleteAppointment(99);

        assertFalse(result);
        verify(repository, never()).deleteById(99);
    }

    @Test
    void toggleAppointmentStatusShouldCancelActiveAppointment() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointment.setStatus(Appointment.Status.ACTIVE);

        when(repository.findById(1)).thenReturn(Optional.of(appointment));

        boolean result = appointmentService.toggleAppointmentStatus(1);

        assertTrue(result);
        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
        verify(repository).save(appointment);
    }

    @Test
    void toggleAppointmentStatusShouldActivateCancelledAppointment() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        Appointment appointment = new Appointment(patient, doctor, appointmentTime);
        appointment.setStatus(Appointment.Status.CANCELLED);

        when(repository.findById(1)).thenReturn(Optional.of(appointment));

        boolean result = appointmentService.toggleAppointmentStatus(1);

        assertTrue(result);
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());
        verify(repository).save(appointment);
    }

    @Test
    void toggleAppointmentStatusShouldReturnFalseWhenAppointmentMissing() {
        AppointmentRepository repository = mock(AppointmentRepository.class);
        ReflectionTestUtils.setField(
                appointmentService,
                "appointmentRepository",
                repository
        );

        when(repository.findById(99)).thenReturn(Optional.empty());

        boolean result = appointmentService.toggleAppointmentStatus(99);

        assertFalse(result);
        verify(repository, never()).save(any(Appointment.class));
    }
}