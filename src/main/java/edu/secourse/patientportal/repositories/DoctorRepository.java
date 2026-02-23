package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
