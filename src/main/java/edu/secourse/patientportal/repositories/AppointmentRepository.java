package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
}
