package edu.sjsu.cmpe172.barbershop.service;

import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;
import edu.sjsu.cmpe172.barbershop.model.Service;
import edu.sjsu.cmpe172.barbershop.repository.*;
import java.util.List;

@org.springframework.stereotype.Service
public class SalonService {
    private final ServiceRepository serviceRepo;
    private final AvailabilitySlotRepository availabilitySlotRepo;

    public SalonService(ServiceRepository serviceRepo, 
        AvailabilitySlotRepository availabilitySlotRepo) {
        this.serviceRepo = serviceRepo;
        this.availabilitySlotRepo = availabilitySlotRepo;    
    }

    public List<Service> getAllServices() {
        return serviceRepo.findAll();
    }

    public List<AvailabilitySlot> getAvailabilitySlots(Long providerId, String date) {
        return availabilitySlotRepo.findAvailabilitySlots(providerId, date);
    }
}
