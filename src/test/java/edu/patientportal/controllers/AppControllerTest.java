package edu.patientportal.controllers;

import edu.secourse.patientportal.controllers.AppController;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Appointment;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppControllerTest {

    @Test
    public void testCreatePatient() {
        AppController controller = new AppController();
        Patient p = controller.createPatient("punika");

        assertNotNull(p);
        assertEquals("punika", p.getUsername());
    }

    @Test
    public void testCreateDoctor() {
        AppController controller = new AppController();
        Doctor d = controller.createDoctor("krouth");

        assertNotNull(d);
        assertEquals("krouth", d.getName());
    }

    @Test
    public void testScheduleAppointment() {
        AppController controller = new AppController();

        Patient p = controller.createPatient("punika");
        Doctor d = controller.createDoctor("miller");

        Appointment appt = controller.scheduleAppointment(p, d, "2025-11-29T10:55");

        assertNotNull(appt);
        assertEquals(p, appt.getPatient());
        assertEquals(d, appt.getDoctor());
    }
}
