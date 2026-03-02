package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
