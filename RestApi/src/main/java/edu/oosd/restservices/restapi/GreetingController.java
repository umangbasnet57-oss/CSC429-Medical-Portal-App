package edu.oosd.restservices.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
//@RequestMapping("/api")
public class GreetingController {
    private static final String template = "Hello %s!";
    private static final String template_home = "Hello! you are at %s";
    private final AtomicLong counter = new AtomicLong();

    // A memory map to store greetings for PUT demonstration
    private final Map<String, Greeting> greetingMap = new HashMap<>();


    @GetMapping("/greeting")
    public EntityModel<Greeting> greeting(@RequestParam(defaultValue = "World") String name){
        String responseBody = "Greeting found.\n";
        Greeting userGreeting = new Greeting(counter.incrementAndGet(), String.format(template, name));

        // Add links
        EntityModel<Greeting> greeting = EntityModel.of(userGreeting);

        // self link -- idea from tutorials at https://spring.io/guides/tutorials/rest
        greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());


        return  greeting;
    }

    @GetMapping("/greeting/{id}")
    public EntityModel<Greeting> getGreeting(@PathVariable Long id, @RequestParam(defaultValue = "World") String name){
        // Fetch data
        Greeting userGreeting = new Greeting(id, String.format(template, name));

        // Add links
        EntityModel<Greeting> greeting = EntityModel.of(userGreeting);

        // self link -- idea from tutorials at https://spring.io/guides/tutorials/rest
        greeting.add(linkTo(methodOn(GreetingController.class).getGreeting(id, name)).withSelfRel());

        // update link
        greeting.add(linkTo(methodOn(GreetingController.class).updateGreeting(id, null)).withRel("update"));

        return  greeting;

    }

    @GetMapping("/")
    public EntityModel<Greeting> home(){
        Greeting userGreeting = new Greeting();

        // Add links
        EntityModel<Greeting> greeting = EntityModel.of(userGreeting);

        // self link -- idea from tutorials at https://spring.io/guides/tutorials/rest
        greeting.add(linkTo(methodOn(GreetingController.class).home()).withSelfRel());

        return greeting;
    }

    @GetMapping("/greeting/")
    public ResponseEntity<String> getResourceById(Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("ID cannot be null"); // Returns 400 Bad Request
        }
        Greeting greeting = new Greeting();

        if (greeting == null) {
            return ResponseEntity.notFound().build(); // Returns 404 Not Found
        } else {
            return ResponseEntity.ok(greeting.toString()); // Returns 200 OK
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createGreeting(@RequestBody Greeting newGreeting) {
//        Greeting createdGreeting =
        return new ResponseEntity<>("New greeting created!", HttpStatus.CREATED);
    }

    @PutMapping("/greeting/{id}")
    public EntityModel<Greeting> updateGreeting(@PathVariable Long id, @RequestBody Greeting updatedGreeting) {
        if (greetingMap.containsKey(id)) {
            Greeting storedGreeting = greetingMap.get(id);
            storedGreeting.setId(updatedGreeting.getId());
            storedGreeting.setContent(updatedGreeting.getContent());

            // Add links
            EntityModel<Greeting> greeting = EntityModel.of(storedGreeting);
            // self link -- idea from tutorials at https://spring.io/guides/tutorials/rest
            greeting.add(linkTo(methodOn(GreetingController.class).updateGreeting(id, storedGreeting)).withSelfRel());

            return greeting; // ResponseEntity.ok(storedGreeting);
        }
        else {
            updatedGreeting.setId(id);
            greetingMap.put(id.toString(), updatedGreeting);

            // Add links
            EntityModel<Greeting> greeting = EntityModel.of(updatedGreeting);
            // self link -- idea from tutorials at https://spring.io/guides/tutorials/rest
            greeting.add(linkTo(methodOn(GreetingController.class).updateGreeting(id, updatedGreeting)).withSelfRel());

            return greeting;// ResponseEntity.status(201).body(updatedGreeting);
        }


    }
}
