package edu.sjsu.cmpe172.barbershop.service;

import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;
import edu.sjsu.cmpe172.barbershop.model.Service;
import edu.sjsu.cmpe172.barbershop.repository.*;
import java.util.List;

/**
 * Represent Service layer in application.
 * Service layer contains business logic and acts as a bridge between 
 * Controller layer and Repo layer
 */
@org.springframework.stereotype.Service
public class SalonService {
    // Repo responsible for getting service info
    private final ServiceRepository serviceRepo;

    // Repo responsible for getting availability slots
    private final AvailabilitySlotRepository availabilitySlotRepo;

    // Constructor 
    public SalonService(ServiceRepository serviceRepo, 
        AvailabilitySlotRepository availabilitySlotRepo) {
        this.serviceRepo = serviceRepo;
        this.availabilitySlotRepo = availabilitySlotRepo;    
    }

    // Get all services offered by salon and return as a list of object
    public List<Service> getAllServices() {
        return serviceRepo.findAll();
    }

    // Retrieves available time slot of a specific provider
    public List<AvailabilitySlot> getAvailabilitySlots(Long providerId, String date) {
        return availabilitySlotRepo.findAvailabilitySlots(providerId, date);
    }
}
