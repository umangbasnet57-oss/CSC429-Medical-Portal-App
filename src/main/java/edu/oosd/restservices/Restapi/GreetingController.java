package edu.oosd.restservices.Restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public ResponseEntity<Greeting> greeting(@RequestParam(defaultValue = "World") String name) {

        Greeting greeting = new Greeting(counter.incrementAndGet(),
                String.format(template, name));

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Welcome to the Home Page");
        response.put("status", "API is running");
        response.put("greeting_endpoint", "http://localhost:8080/greeting");
        response.put("greeting_with_name", "http://localhost:8080/greeting?name=YourName");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
