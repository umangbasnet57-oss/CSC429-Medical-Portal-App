package edu.secourse.patientportal.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Test
    void defaultConstructorShouldSetDefaultValues() {
        Appointment appointment = new Appointment();

        assertEquals(0, appointment.getAppointmentId());
        assertNotNull(appointment.getPatient());
        assertNotNull(appointment.getDoctor());
        assertEquals(LocalDateTime.MIN, appointment.getAppointmentDateTime());
        assertEquals(LocalDateTime.MIN, appointment.getEndTime());
        assertNull(appointment.getLastUpdated());
        assertEquals(Appointment.Status.COMPLETED, appointment.getStatus());
    }

    @Test
    void constructorShouldSetFieldsWhenValidValuesAreGiven() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");
        LocalDateTime start = LocalDateTime.of(2026, 5, 8, 10, 0);

        Appointment appointment = new Appointment(patient, doctor, start);

        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(start, appointment.getAppointmentDateTime());
        assertEquals(start.plusMinutes(30), appointment.getEndTime());
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());
        assertNotNull(appointment.getLastUpdated());
    }

    @Test
    void constructorShouldKeepDefaultsWhenPatientIsNull() {
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");
        LocalDateTime start = LocalDateTime.of(2026, 5, 8, 10, 0);

        Appointment appointment = new Appointment(null, doctor, start);

        assertNotNull(appointment.getPatient());
        assertEquals(LocalDateTime.MIN, appointment.getAppointmentDateTime());
        assertEquals(LocalDateTime.MIN, appointment.getEndTime());
        assertEquals(Appointment.Status.COMPLETED, appointment.getStatus());
        assertNull(appointment.getLastUpdated());
    }

    @Test
    void constructorShouldKeepDefaultsWhenDoctorIsNull() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        LocalDateTime start = LocalDateTime.of(2026, 5, 8, 10, 0);

        Appointment appointment = new Appointment(patient, null, start);

        assertNotNull(appointment.getDoctor());
        assertEquals(LocalDateTime.MIN, appointment.getAppointmentDateTime());
        assertEquals(LocalDateTime.MIN, appointment.getEndTime());
        assertEquals(Appointment.Status.COMPLETED, appointment.getStatus());
        assertNull(appointment.getLastUpdated());
    }

    @Test
    void constructorShouldKeepDefaultsWhenDateTimeIsNull() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");

        Appointment appointment = new Appointment(patient, doctor, null);

        assertEquals(LocalDateTime.MIN, appointment.getAppointmentDateTime());
        assertEquals(LocalDateTime.MIN, appointment.getEndTime());
        assertEquals(Appointment.Status.COMPLETED, appointment.getStatus());
        assertNull(appointment.getLastUpdated());
    }

    @Test
    void setAppointmentIdShouldUpdateValidId() {
        Appointment appointment = new Appointment();

        assertTrue(appointment.setAppointmentId(10));
        assertEquals(10, appointment.getAppointmentId());
    }

    @Test
    void setAppointmentIdShouldRejectIntegerMinValue() {
        Appointment appointment = new Appointment();

        assertFalse(appointment.setAppointmentId(Integer.MIN_VALUE));
        assertEquals(0, appointment.getAppointmentId());
    }

    @Test
    void setAppointmentIdShouldRejectIntegerMaxValue() {
        Appointment appointment = new Appointment();

        assertFalse(appointment.setAppointmentId(Integer.MAX_VALUE));
        assertEquals(0, appointment.getAppointmentId());
    }

    @Test
    void setPatientShouldUpdatePatientAndLastUpdated() {
        Appointment appointment = new Appointment();
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");

        assertTrue(appointment.setPatient(patient));

        assertEquals(patient, appointment.getPatient());
        assertNotNull(appointment.getLastUpdated());
    }

    @Test
    void setPatientShouldRejectNull() {
        Appointment appointment = new Appointment();
        Patient originalPatient = appointment.getPatient();

        assertFalse(appointment.setPatient(null));
        assertEquals(originalPatient, appointment.getPatient());
    }

    @Test
    void setDoctorShouldUpdateDoctorAndLastUpdated() {
        Appointment appointment = new Appointment();
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");

        assertTrue(appointment.setDoctor(doctor));

        assertEquals(doctor, appointment.getDoctor());
        assertNotNull(appointment.getLastUpdated());
    }

    @Test
    void setDoctorShouldRejectNull() {
        Appointment appointment = new Appointment();
        Doctor originalDoctor = appointment.getDoctor();

        assertFalse(appointment.setDoctor(null));
        assertEquals(originalDoctor, appointment.getDoctor());
    }

    @Test
    void setAppointmentDateTimeShouldUpdateStartEndAndLastUpdated() {
        Appointment appointment = new Appointment();
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 12, 0);

        assertTrue(appointment.setAppointmentDateTime(start));

        assertEquals(start, appointment.getAppointmentDateTime());
        assertEquals(start.plusMinutes(30), appointment.getEndTime());
        assertNotNull(appointment.getLastUpdated());
    }

    @Test
    void setAppointmentDateTimeShouldRejectNull() {
        Appointment appointment = new Appointment();

        assertFalse(appointment.setAppointmentDateTime(null));
        assertEquals(LocalDateTime.MIN, appointment.getAppointmentDateTime());
    }

    @Test
    void setEndTimeShouldUpdateEndTimeAndLastUpdated() {
        Appointment appointment = new Appointment();
        LocalDateTime end = LocalDateTime.of(2026, 6, 1, 12, 30);

        assertTrue(appointment.setEndTime(end));

        assertEquals(end, appointment.getEndTime());
        assertNotNull(appointment.getLastUpdated());
    }

    @Test
    void setEndTimeShouldRejectNull() {
        Appointment appointment = new Appointment();

        assertFalse(appointment.setEndTime(null));
        assertEquals(LocalDateTime.MIN, appointment.getEndTime());
    }

    @Test
    void setStatusShouldUpdateStatus() {
        Appointment appointment = new Appointment();

        assertTrue(appointment.setStatus(Appointment.Status.ACTIVE));
        assertEquals(Appointment.Status.ACTIVE, appointment.getStatus());
    }

    @Test
    void setStatusShouldRejectNull() {
        Appointment appointment = new Appointment();

        assertFalse(appointment.setStatus(null));
        assertEquals(Appointment.Status.COMPLETED, appointment.getStatus());
    }

    @Test
    void cancelAppointmentShouldSetStatusToCancelled() {
        Appointment appointment = new Appointment();

        appointment.cancelAppointment();

        assertEquals(Appointment.Status.CANCELLED, appointment.getStatus());
    }

    @Test
    void toStringShouldContainAppointmentDetails() {
        Patient patient = new Patient("patient1", "pass", "Patient One", "p@email.com");
        Doctor doctor = new Doctor("doctor1", "pass", "Doctor One", "d@email.com");
        LocalDateTime start = LocalDateTime.of(2026, 5, 8, 10, 0);

        Appointment appointment = new Appointment(patient, doctor, start);
        appointment.setAppointmentId(15);

        String result = appointment.toString();

        assertTrue(result.contains("15"));
        assertTrue(result.contains("patient1"));
        assertTrue(result.contains("doctor1"));
        assertTrue(result.contains("ACTIVE"));
        assertTrue(result.contains(start.toString()));
    }

    @Test
    void toStringShouldHandleNullPatientDoctorAndDateTime() {
        Appointment appointment = new Appointment();

        appointment.setPatient(new Patient("tempPatient", "pass", "Temp Patient", "p@email.com"));
        appointment.setDoctor(new Doctor("tempDoctor", "pass", "Temp Doctor", "d@email.com"));

        // Force nulls using reflection because setters reject null
        org.springframework.test.util.ReflectionTestUtils.setField(appointment, "patient", null);
        org.springframework.test.util.ReflectionTestUtils.setField(appointment, "doctor", null);
        org.springframework.test.util.ReflectionTestUtils.setField(appointment, "appointmentDateTime", null);
        org.springframework.test.util.ReflectionTestUtils.setField(appointment, "endTime", null);

        String result = appointment.toString();

        assertTrue(result.contains("Unknown Patient"));
        assertTrue(result.contains("Unknown Doctor"));
        assertTrue(result.contains("No Date"));
        assertTrue(result.contains("No End Time"));
    }
}