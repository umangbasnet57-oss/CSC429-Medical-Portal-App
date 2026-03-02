package edu.secourse.patientportal.modelassemblers;

import edu.secourse.patientportal.controllers.AdminController;
import edu.secourse.patientportal.models.Admin;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AdminModelAssembler implements RepresentationModelAssembler<Admin, EntityModel<Admin>> {
    public EntityModel<Admin> toModel(Admin admin) {
        return EntityModel.of(admin, //
                linkTo(methodOn(AdminController.class).getOneAdministrator(admin.getAdminId())).withSelfRel(),
                linkTo(methodOn(AdminController.class).getAllAdministrators()).withRel("administrators"));
    }
}
