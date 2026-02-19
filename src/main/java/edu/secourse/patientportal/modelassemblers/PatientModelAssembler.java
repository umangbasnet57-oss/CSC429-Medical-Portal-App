package edu.secourse.patientportal.modelassemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import edu.secourse.patientportal.controllers.PatientController;
import edu.secourse.patientportal.models.Patient;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PatientModelAssembler implements  RepresentationModelAssembler<Patient, EntityModel<Patient>> {
    public EntityModel<Patient> toModel(Patient patient){
        return EntityModel.of(patient, //
                linkTo(methodOn(PatientController.class).one(patient.getPatientId())).withSelfRel(),
                linkTo(methodOn(PatientController.class).all()).withRel("employees"));
    }
}
