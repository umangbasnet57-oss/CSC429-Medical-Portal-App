package edu.secourse.patientportal.services;

import edu.secourse.patientportal.dto.PatientDTO;
import edu.secourse.patientportal.models.Patient;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface PatientService {
    Patient createPatient(PatientDTO patientDTO);
    List<EntityModel<Patient>> getAllPatients();
    Patient getOnePatient(Integer id);
}
