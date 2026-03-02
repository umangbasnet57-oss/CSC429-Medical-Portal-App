package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer>{
}
