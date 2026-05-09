package edu.secourse.patientportal.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void defaultConstructorShouldCreatePatientObject() {
        Patient patient = new Patient();

        assertNotNull(patient);
        assertEquals("", patient.getUsername());
        assertEquals("", patient.getPassword());
        assertEquals("", patient.getName());
        assertEquals("", patient.getEmail());
    }

    @Test
    void constructorShouldSetPatientFields() {
        Patient patient = new Patient(
                "patient1",
                "hashedPassword",
                "Patient One",
                "patient@email.com"
        );

        assertEquals("patient1", patient.getUsername());
        assertEquals("hashedPassword", patient.getPassword());
        assertEquals("Patient One", patient.getName());
        assertEquals("patient@email.com", patient.getEmail());
        assertEquals("PATIENT", patient.getRole());
    }

    @Test
    void constructorShouldSplitFirstAndLastName() {
        Patient patient = new Patient(
                "patient2",
                "pass",
                "John Doe",
                "john@email.com"
        );

        assertEquals("John", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
    }

    @Test
    void patientShouldUseInheritedSetters() {
        Patient patient = new Patient();

        patient.setUsername("newpatient");
        patient.setPassword("newpass");
        patient.setName("Jane Smith");
        patient.setEmail("jane@email.com");
        patient.setRole("patient");

        assertEquals("newpatient", patient.getUsername());
        assertEquals("newpass", patient.getPassword());
        assertEquals("Jane Smith", patient.getName());
        assertEquals("jane@email.com", patient.getEmail());
        assertEquals("PATIENT", patient.getRole());

        assertEquals("Jane", patient.getFirstName());
        assertEquals("Smith", patient.getLastName());

        assertNotNull(patient.getLastPasswordChange());
    }

    @Test
    void patientShouldBeInstanceOfUser() {
        Patient patient = new Patient();

        assertTrue(patient instanceof User);
    }

    @Test
    void patientsWithSameUsernameShouldBeEqual() {
        Patient patient1 = new Patient(
                "samePatient",
                "pass1",
                "Patient One",
                "one@email.com"
        );

        Patient patient2 = new Patient(
                "samePatient",
                "pass2",
                "Patient Two",
                "two@email.com"
        );

        assertEquals(patient1, patient2);
    }

    @Test
    void toStringShouldContainPatientInformation() {
        Patient patient = new Patient(
                "patient3",
                "pass",
                "Patient Three",
                "patient3@email.com"
        );

        String result = patient.toString();

        assertTrue(result.contains("patient3"));
        assertTrue(result.contains("Patient Three"));
        assertTrue(result.contains("patient3@email.com"));
    }

    @Test
    void recordLoginShouldSetLastLogin() {
        Patient patient = new Patient();

        assertNull(patient.getLastLogin());

        patient.recordLogin();

        assertNotNull(patient.getLastLogin());
    }
}