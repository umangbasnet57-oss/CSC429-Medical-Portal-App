package edu.secourse.patientportal.services;

import edu.secourse.patientportal.dto.AdminDTO;
import edu.secourse.patientportal.models.Admin;
import org.springframework.hateoas.EntityModel;

import java.util.List;

public interface AdminService {
    Admin createAdmin(AdminDTO adminDTO);
    List<EntityModel<Admin>> getAllAdministrators();
    Admin getOneAdministrator(Integer id);
}
