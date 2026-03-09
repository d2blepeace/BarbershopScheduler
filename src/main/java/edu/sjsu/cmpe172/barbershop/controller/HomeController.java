package edu.sjsu.cmpe172.barbershop.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class provides root endpoint of the web
 * For now it only a verification that SpringBoot app is running
 * 
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Vintage Razor Barbershop & Salon API is running.";
    }
}
