package edu.secourse.patientportal.routes;

import edu.secourse.patientportal.assemblers.UserModelAssembler;
import edu.secourse.patientportal.models.Appointment;
import edu.secourse.patientportal.models.User;
import edu.secourse.patientportal.repositories.UserRepository;
import edu.secourse.patientportal.repositories.UserRepositoryImpl;
import edu.secourse.patientportal.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.List;


@RestController
@RequestMapping("/maclogixapi/v1")
public class UserRoute {
    private final UserRepository repository;
    private final UserModelAssembler assembler;
    private final UserService service;

    public UserRoute(UserRepositoryImpl repository, UserModelAssembler assembler, UserService service){
        this.repository = repository;
        this.assembler = assembler;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        boolean userCreated = service.createUser(user);
        User createdUser = service.getUser(user.getUsername());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //    Single items
//    @GetMapping({"/doctors/{id}"})
//    public EntityModel<User> getDoctor(@PathVariable Integer id){
//        Doctor doctor = (Doctor) service.getUserById(id);
//
//        return EntityModel.of(doctor,//
//                linkTo(methodOn(UserRoute.class).getDoctor(id)).withSelfRel(),
//                linkTo(methodOn(UserRoute.class).getAllDoctors()).withRel("doctors")
//        );
//    }


//    @GetMapping("/doctors")
//    public CollectionModel<EntityModel<User>> getAllDoctors(){
//
//        List<EntityModel<User>> doctors = service.getAllUsers();
//
//        return CollectionModel.of(doctors, linkTo(methodOn(UserRoute.class).getAllDoctors()).withSelfRel());
//    }

//    @GetMapping("/patients/{id}")
//    public EntityModel<User> getPatient(@PathVariable Integer id){
//        Patient patient = (Patient) service.getUserById(id);
//
//        return EntityModel.of(patient,//
//                linkTo(methodOn(UserRoute.class).getPatient(id)).withSelfRel(),
//                linkTo(methodOn(UserRoute.class).getAllPatients()).withRel("patients")
//        );
//    }

//    @GetMapping("/patients")
//    public CollectionModel<EntityModel<User>> getAllPatients(){
//        List<EntityModel<User>> patients = service.getAllUsers();
//
//        return CollectionModel.of(patients, linkTo(methodOn(UserRoute.class).getAllPatients()).withSelfRel());
//
//    }

//    @GetMapping("/patients/{id}")
//    public EntityModel<Appointment> getOneUser(@PathVariable Integer id){
//        Appointment appointment = service.getOneAppointment(id);
//
//        return assembler.toModel(appointment);
//    }

    @GetMapping("/users/{id}")
    public  EntityModel<User> getUser(@PathVariable Integer id){
        User user = service.getUserById(id);
        if (user == null) {
//            return ResponseEntity.notFound().build();
            System.out.println("User is null");
        }

        return assembler.toModel(user);
//        return EntityModel.of(user,//
//                linkTo(methodOn(UserRoute.class).getUser(id)).withSelfRel(),
//                linkTo(methodOn(UserRoute.class).getAllUsers()).withRel("users")
//        );
    }


    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> getAllUsers(){
        List<User> users = service.getAllUsers();
        return assembler.toCollectionModel(users);

//        List<EntityModel<User>> users = repository.findAll().stream()
//                .map(user -> EntityModel.of(user,
//                        linkTo(methodOn(UserRoute.class).getUser(user.getUserId())).withSelfRel(),
//                        linkTo(methodOn(UserRoute.class).getAllUsers()).withRel("users")))
//                .collect(Collectors.toList());
//        return assembler.toCollectionModel(users);
//        return CollectionModel.of(users, linkTo(methodOn(UserRoute.class).getAllUsers()).withSelfRel());
    }

//    @GetMapping("/users/{id}")
//    public EntityModel<User> getOneUser(@PathVariable Integer id){
//        return assembler.toModel(service.getUserById(id));
//    }

    @PutMapping("{id}")
    public Appointment modifyAppointment(@RequestBody Appointment newAppointemnt, Integer id){
        return newAppointemnt;
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}
