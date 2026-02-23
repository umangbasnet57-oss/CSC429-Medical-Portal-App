package edu.secourse.patientportal.controllers;

import edu.secourse.patientportal.dto.PatientDTO;
import edu.secourse.patientportal.exceptions.PatientNotFoundException;
import edu.secourse.patientportal.modelassemblers.PatientModelAssembler;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.repositories.PatientRepository;
import java.util.stream.Collectors;

import edu.secourse.patientportal.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/maclogixapi")
public class PatientController {
    private final PatientRepository repository;
    private final PatientModelAssembler assembler;
    private final PatientService service;

    public PatientController(PatientRepository repository, PatientModelAssembler assembler, PatientService service){
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/patient-home")
    public String patientWelcome(){
        return "Welcome to Patient Portal.";
    }
    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDTO patientDTO){
        Patient patient = service.createPatient(patientDTO);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

//    @PostMapping
//    public EntityModel<Patient> createPatient(@RequestBody PatientDTO patientDTO){
//        Patient patient = patientService.createPatient(patientDTO);
////        return new ResponseEntity<>(patient, HttpStatus.CREATED);
//        return assembler.toModel(patient);
//    }

    @GetMapping("/patients")
    public CollectionModel<EntityModel<Patient>> getAllPatients(){
        List<EntityModel<Patient>> patients = service.getAllPatients();

        return CollectionModel.of(patients, linkTo(methodOn(PatientController.class).getAllPatients()).withSelfRel());
    }

//    Single item
    @GetMapping("/patients/{id}")
    public EntityModel<Patient> getOnePatient(@PathVariable Integer id){
        Patient patient = service.getOnePatient(id);

        return assembler.toModel(patient);
    }

    @PutMapping("/patients/{id}")
    public Patient replacePatient(@RequestBody Patient newPatient, @PathVariable Integer id){
        return new Patient();
    }

    @DeleteMapping("/patients/{id}")
    public void deletePatient(@PathVariable Integer id){
        repository.deleteById(id);
    }
}
