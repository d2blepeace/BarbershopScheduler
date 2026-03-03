package edu.sjsu.cmpe172.barbershop.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Barbershop Scheduler API is running.";
    }
}
