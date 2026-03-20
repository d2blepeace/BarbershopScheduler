package edu.sjsu.cmpe172.barbershop.controller;

import edu.sjsu.cmpe172.barbershop.model.Service;
import edu.sjsu.cmpe172.barbershop.service.SalonService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller handles HTTP request relate to salon services
 * Controller will communicate with Service Layer (SalonService),
 * then retrieves data from Repo Layer
 * Client Request -> Controller -> SalonService -> ServiceRepo -> MockingData
 */
@RestController
public class ServiceController {
    private final SalonService salonService;
    
    //Constructor 
    public ServiceController(SalonService salonService) {
        this.salonService = salonService;
    }

    // Return list of all services available of the system
    @GetMapping("/services") 
    public List<Service> getServices() {
        return salonService.getAllServices();
    }
}
