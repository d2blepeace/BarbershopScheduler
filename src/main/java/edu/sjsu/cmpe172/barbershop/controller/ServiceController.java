package edu.sjsu.cmpe172.barbershop.controller;

import edu.sjsu.cmpe172.barbershop.model.Service;
import edu.sjsu.cmpe172.barbershop.service.SalonService;
//import jakarta.websocket.server.ServerEndpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServiceController {
    private final SalonService salonService;
    public ServiceController(SalonService salonService) {
        this.salonService = salonService;
    }
    @GetMapping("/Services") 
    public List<Service> getServices() {
        return salonService.getAllServices();
    }
}
