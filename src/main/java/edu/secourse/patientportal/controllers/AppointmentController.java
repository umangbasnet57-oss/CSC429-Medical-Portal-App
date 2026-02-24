package edu.secourse.patientportal.controllers;

import edu.secourse.patientportal.dto.AppointmentDTO;
import edu.secourse.patientportal.modelassemblers.AppointmentModelAssembler;
import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.Patient;
import edu.secourse.patientportal.repositories.AppointmentRepository;
import edu.secourse.patientportal.services.AppointmentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/maclogixapi/v1/appointments")
public class AppointmentController {
    private final AppointmentRepository repository;
    private final AppointmentModelAssembler assembler;
    private final AppointmentService service;

    public AppointmentController(AppointmentRepository repository, AppointmentModelAssembler assembler, AppointmentService service){
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO appointmentDTO){
        Appointment appointment = service.createAppointment(appointmentDTO);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

//    Single item
    @GetMapping("/{id}")
    public EntityModel<Appointment> getOneAppointment(@PathVariable Integer id){
        Appointment appointment = service.getOneAppointment(id);

        return assembler.toModel(appointment);
    }

    @GetMapping
    public CollectionModel<EntityModel<Appointment>> getAllAppointments(){
        List<EntityModel<Appointment>> appointments = service.getAllAppointments();

        return CollectionModel.of(appointments, linkTo(methodOn(AppointmentController.class).getAllAppointments()).withSelfRel());
    }

    @PutMapping("{id}")
    public Appointment modifyAppointment(@RequestBody Appointment newAppointemnt, Integer id){
        return newAppointemnt;
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Integer id) {
        repository.deleteById(id);
    }



}
