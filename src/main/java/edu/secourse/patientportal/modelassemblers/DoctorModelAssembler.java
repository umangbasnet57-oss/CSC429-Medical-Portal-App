package edu.secourse.patientportal.modelassemblers;

import edu.secourse.patientportal.controllers.DoctorController;
import edu.secourse.patientportal.models.Doctor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DoctorModelAssembler implements RepresentationModelAssembler<Doctor, EntityModel<Doctor>> {
    public EntityModel<Doctor> toModel(Doctor doctor) {
        return EntityModel.of(doctor, //
                linkTo(methodOn(DoctorController.class).getOneDoctor(doctor.getDoctorId())).withSelfRel(),
                linkTo(methodOn(DoctorController.class).getAllDoctors()).withRel("doctors"));
    }
}
