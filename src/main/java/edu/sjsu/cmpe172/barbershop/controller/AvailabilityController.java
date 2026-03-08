package edu.sjsu.cmpe172.barbershop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;
import edu.sjsu.cmpe172.barbershop.service.SalonService;

@RestController
public class AvailabilityController {
    private final SalonService salonService;

    public AvailabilityController(SalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping("/slots")
    public List<AvailabilitySlot> geAvailabilitySlots(
            @RequestParam Long providerId, 
            @RequestParam String date) {
        return salonService.geAvailabilitySlots(providerId, date);
    }
}
