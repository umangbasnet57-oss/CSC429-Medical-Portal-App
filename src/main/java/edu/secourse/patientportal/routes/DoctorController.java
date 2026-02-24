package edu.secourse.patientportal.routes;

import edu.secourse.patientportal.dto.DoctorDTO;
import edu.secourse.patientportal.modelassemblers.DoctorModelAssembler;
import edu.secourse.patientportal.models.Doctor;
import edu.secourse.patientportal.repositories.DoctorRepository;
import edu.secourse.patientportal.services.DoctorService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/maclogixapi/v1/doctors")
public class DoctorController {
    private final DoctorRepository repository;
    private final DoctorModelAssembler assembler;
    private final DoctorService service;

    public DoctorController(DoctorRepository repository, DoctorModelAssembler assembler, DoctorService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/home")
    public String doctorWelcome() {
        return "Welcome to Doctor Portal.";
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = service.createDoctor(doctorDTO);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    @GetMapping
    public CollectionModel<EntityModel<Doctor>> getAllDoctors() {
        List<EntityModel<Doctor>> doctors = service.getAllDoctors();

        return CollectionModel.of(doctors, linkTo(methodOn(DoctorController.class).getAllDoctors()).withSelfRel());
    }

    //    Single item
    @GetMapping("/{id}")
    public EntityModel<Doctor> getOneDoctor(@PathVariable Integer id) {
        Doctor doctor = service.getOneDoctor(id);

        return assembler.toModel(doctor);
    }

    @PutMapping("/{id}")
    public Doctor replaceDoctor(@RequestBody Doctor newDoctor, @PathVariable Integer id) {
        return new Doctor();
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
