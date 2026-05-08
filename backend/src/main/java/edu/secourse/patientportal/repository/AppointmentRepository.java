package edu.secourse.patientportal.repository;

import edu.secourse.patientportal.model.Appointment;
import edu.secourse.patientportal.model.Doctor;
import edu.secourse.patientportal.model.Patient;
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
