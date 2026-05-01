package edu.sjsu.cmpe172.barbershop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe172.barbershop.model.Provider;
import edu.sjsu.cmpe172.barbershop.service.SalonService;

/**
 * Handles HTTP requests related to service providers (barbers, nail technicians)
 * Flow: Client Request -> Controller -> SalonService -> ProviderRepo -> Database
 */
@RestController
public class ProviderController {
    private final SalonService salonService;

    public ProviderController(SalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping("/providers")
    public List<Provider> getProviders() {
        return salonService.getAllProviders();
    }
}
