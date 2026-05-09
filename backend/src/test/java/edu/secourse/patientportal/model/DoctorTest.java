package edu.secourse.patientportal.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    @Test
    void defaultConstructorShouldCreateDoctorObject() {
        Doctor doctor = new Doctor();

        assertNotNull(doctor);
        assertEquals("", doctor.getUsername());
        assertEquals("", doctor.getPassword());
        assertEquals("", doctor.getName());
        assertEquals("", doctor.getEmail());
    }

    @Test
    void constructorShouldSetDoctorFields() {
        Doctor doctor = new Doctor(
                "doctor1",
                "hashedPassword",
                "Doctor Strange",
                "doctor@email.com"
        );

        assertEquals("doctor1", doctor.getUsername());
        assertEquals("hashedPassword", doctor.getPassword());
        assertEquals("Doctor Strange", doctor.getName());
        assertEquals("doctor@email.com", doctor.getEmail());
        assertEquals("DOCTOR", doctor.getRole());
    }

    @Test
    void constructorShouldSplitFirstAndLastName() {
        Doctor doctor = new Doctor(
                "doctor2",
                "pass",
                "John Smith",
                "johnsmith@email.com"
        );

        assertEquals("John", doctor.getFirstName());
        assertEquals("Smith", doctor.getLastName());
    }

    @Test
    void doctorShouldUseInheritedSetters() {
        Doctor doctor = new Doctor();

        doctor.setUsername("newdoctor");
        doctor.setPassword("newpass");
        doctor.setName("Jane Wilson");
        doctor.setEmail("jane@email.com");
        doctor.setRole("doctor");

        assertEquals("newdoctor", doctor.getUsername());
        assertEquals("newpass", doctor.getPassword());
        assertEquals("Jane Wilson", doctor.getName());
        assertEquals("jane@email.com", doctor.getEmail());
        assertEquals("DOCTOR", doctor.getRole());
        assertEquals("Jane", doctor.getFirstName());
        assertEquals("Wilson", doctor.getLastName());

        assertNotNull(doctor.getLastPasswordChange());
    }

    @Test
    void doctorShouldBeInstanceOfUser() {
        Doctor doctor = new Doctor();

        assertTrue(doctor instanceof User);
    }

    @Test
    void doctorsWithSameUsernameShouldBeEqual() {
        Doctor doctor1 = new Doctor(
                "sameDoctor",
                "pass1",
                "Doctor One",
                "one@email.com"
        );

        Doctor doctor2 = new Doctor(
                "sameDoctor",
                "pass2",
                "Doctor Two",
                "two@email.com"
        );

        assertEquals(doctor1, doctor2);
    }

    @Test
    void toStringShouldContainDoctorInformation() {
        Doctor doctor = new Doctor(
                "doctor3",
                "pass",
                "Doctor House",
                "house@email.com"
        );

        String result = doctor.toString();

        assertTrue(result.contains("doctor3"));
        assertTrue(result.contains("Doctor House"));
        assertTrue(result.contains("house@email.com"));
    }
}