package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
