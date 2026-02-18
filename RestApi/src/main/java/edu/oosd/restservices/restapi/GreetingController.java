package edu.oosd.restservices.RestApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Welcome to RestApi");
        body.put("status", "OK");
        body.put("hrefs", Map.of(
                "greeting_default", "/greeting",
                "greeting_with_name_example", "/greeting?name=Madhav"
        ));

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/greeting")
    public ResponseEntity<Map<String, Object>> greeting(
            @RequestParam(defaultValue = "World") String name
    ) {
        long id = counter.incrementAndGet();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", id);
        body.put("content", String.format(template, name));

        // "Different status messages" example:
        if ("World".equalsIgnoreCase(name)) {
            body.put("status", "DEFAULT_NAME_USED");
            body.put("note", "No name provided; using default.");
            body.put("hrefs", Map.of(
                    "self", "/greeting",
                    "try_with_name", "/greeting?name=Madhav",
                    "home", "/"
            ));
            return ResponseEntity.status(HttpStatus.OK).body(body);
        } else {
            body.put("status", "CUSTOM_NAME_USED");
            body.put("hrefs", Map.of(
                    "self", "/greeting?name=" + name,
                    "home", "/"
            ));
            return ResponseEntity.status(HttpStatus.OK).body(body);
        }
    }
}
