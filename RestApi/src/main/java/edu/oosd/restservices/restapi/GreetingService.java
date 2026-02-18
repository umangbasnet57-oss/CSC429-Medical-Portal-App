package edu.oosd.restservices.restapi;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GreetingService {
    private final List<Greeting> books = new ArrayList<>();
    private final AtomicInteger idGen = new AtomicInteger(0);
}
