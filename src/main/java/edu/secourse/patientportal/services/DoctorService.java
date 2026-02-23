package edu.secourse.patientportal.services;

import edu.secourse.patientportal.dto.DoctorDTO;
import edu.secourse.patientportal.models.Doctor;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface DoctorService {
    Doctor createDoctor(DoctorDTO doctorDTO);
    List<EntityModel<Doctor>> getAllDoctors();
    Doctor getOneDoctor(Integer id);
}
