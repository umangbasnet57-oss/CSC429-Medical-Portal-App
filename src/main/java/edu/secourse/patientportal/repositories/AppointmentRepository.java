package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    /** Tous les rendez-vous d'un patient. */
    List<Appointment> findByPatient(Patient patient);

    /** Tous les rendez-vous d'un médecin. */
    List<Appointment> findByDoctor(Doctor doctor);
}
