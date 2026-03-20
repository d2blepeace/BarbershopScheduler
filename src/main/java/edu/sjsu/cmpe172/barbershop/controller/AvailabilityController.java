package edu.sjsu.cmpe172.barbershop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;
import edu.sjsu.cmpe172.barbershop.service.SalonService;

/**
 * This ccontroller handles request related to available time slots
 * Customer can query available times for a specific provider on a given date
 * NOTES: need refining to work better
 */
@RestController
public class AvailabilityController {
    private final SalonService salonService;

    public AvailabilityController(SalonService salonService) {
        this.salonService = salonService;
    }

    /**
     * Return the available appointment slots for a specific provider
     * @param providerId - barber/technician ID
     * @param date       - date to check for availability (YYYY-MM-DD)
     * @return available slots
     */
    @GetMapping("/slots")
    public List<AvailabilitySlot> getAvailabilitySlots(
            @RequestParam Long providerId, 
            @RequestParam String date) {
        return salonService.getAvailabilitySlots(providerId, date);
    }
}
