package edu.secourse.patientportal.repositories;

import edu.secourse.patientportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /** Trouve un utilisateur par son username. */
    Optional<User> findByUsername(String username);

    /** Vérifie si un username est déjà utilisé. */
    boolean existsByUsername(String username);
}
