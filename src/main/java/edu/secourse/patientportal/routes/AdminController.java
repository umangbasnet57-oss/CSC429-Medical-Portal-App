package edu.secourse.patientportal.routes;

import edu.secourse.patientportal.dto.AdminDTO;
import edu.secourse.patientportal.modelassemblers.AdminModelAssembler;
import edu.secourse.patientportal.models.Admin;
import edu.secourse.patientportal.repositories.AdminRepository;

import edu.secourse.patientportal.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/maclogixapi")
public class AdminController {
    private final AdminRepository repository;
    private final AdminModelAssembler assembler;
    private final AdminService service;

    public AdminController(AdminRepository repository, AdminModelAssembler assembler, AdminService service) {
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @GetMapping("/admin-home")
    public String adminWelcome() {
        return "Welcome to Admin Portal.";
    }

    @PostMapping("/administrators")
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminDTO adminDTO) {
        Admin admin = service.createAdmin(adminDTO);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }


    @GetMapping("/administrators")
    public CollectionModel<EntityModel<Admin>> getAllAdministrators() {
        List<EntityModel<Admin>> administrators = service.getAllAdministrators();

        return CollectionModel.of(administrators, linkTo(methodOn(AdminController.class).getAllAdministrators()).withSelfRel());
    }

    //    Single item
    @GetMapping("/administrators/{id}")
    public EntityModel<Admin> getOneAdministrator(@PathVariable Integer id) {
        Admin admin = service.getOneAdministrator(id);

        return assembler.toModel(admin);
    }

    @PutMapping("/administrators/{id}")
    public Admin replaceAdmin(@RequestBody Admin newAdmin, @PathVariable Integer id) {
        return new Admin();
    }

    @DeleteMapping("/administrators/{id}")
    public void deleteAdmin(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
