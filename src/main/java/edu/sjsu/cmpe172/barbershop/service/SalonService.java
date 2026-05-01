package edu.sjsu.cmpe172.barbershop.service;

import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;
import edu.sjsu.cmpe172.barbershop.model.Provider;
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
    private final ServiceRepository serviceRepo;
    private final AvailabilitySlotRepository availabilitySlotRepo;
    private final ProviderRepository providerRepo;


    // Constructor 
    public SalonService(ServiceRepository serviceRepo, 
        AvailabilitySlotRepository availabilitySlotRepo,
        ProviderRepository providerRepo) {
        this.serviceRepo = serviceRepo;
        this.availabilitySlotRepo = availabilitySlotRepo;
        this.providerRepo = providerRepo;  
    }

    // Get all services offered by salon and return as a list of object
    public List<Service> getAllServices() {
        return serviceRepo.findAll();
    }

    // Retrieves available time slot of a specific provider
    public List<AvailabilitySlot> getAvailabilitySlots(Long providerId, String date) {
        return availabilitySlotRepo.findAvailabilitySlots(providerId, date);
    }

    // Get all providers (barbers and nail technicians)
    public List<Provider> getAllProviders() {
        return providerRepo.findAll();
    }
}
