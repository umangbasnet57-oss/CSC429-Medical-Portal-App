package edu.secourse.patientportal.controllers;

import edu.secourse.patientportal.modelassemblers.PatientModelAssembler;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.repositories.PatientRepository;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.EntityModel;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class PatientController {
    private final PatientRepository repository;
    private final PatientModelAssembler assembler;

    public PatientController(PatientRepository repository, PatientModelAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/patients")
    public CollectionModel<EntityModel<Patient>> all(){
        List<EntityModel<Patient>> patients = repository.findAll().stream()
                .map(patient -> EntityModel.of(patient,
                        linkTo(methodOn(PatientController.class).one(patient.getPatientId())).withSelfRel(),
                        linkTo(methodOn(PatientController.class).all()).withRel("patient")))
                .collect(Collectors.toList());

        return CollectionModel.of(patients, linkTo(methodOn(PatientController.class).all()).withSelfRel());
    }

//    Single item
    @GetMapping("/patients/{id}")
    public EntityModel<Patient> one(@PathVariable Integer id){
        Patient patient = new Patient();

        return EntityModel.of(patient,
                linkTo(methodOn(PatientController.class).one(id)).withSelfRel(),
                linkTo(methodOn(PatientController.class).all()).withRel("patients")
        );
    }
}
