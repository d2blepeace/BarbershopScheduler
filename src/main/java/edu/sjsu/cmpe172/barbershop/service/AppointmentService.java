package edu.sjsu.cmpe172.barbershop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe172.barbershop.model.Appointment;
import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;
import edu.sjsu.cmpe172.barbershop.model.Service;
import edu.sjsu.cmpe172.barbershop.repository.AppointmentRepository;
import edu.sjsu.cmpe172.barbershop.repository.AvailabilitySlotRepository;
import edu.sjsu.cmpe172.barbershop.repository.ServiceRepository;

/**
 * Service layer for Appointment Logic
 * 
 * Responsibility:
 *  - Validate booking request
 *  - Enforce business rule
 *  - coordinate multiple repo
 *  - ensure data consistent
 * 
 * Flow: Controller -> AppointmentService -> Repo + Database
 */
public class AppointmentService {
    private final AppointmentRepository appointmentRepo;
    private final AvailabilitySlotRepository availSlotRepo;
    private final ServiceRepository serviceRepo;

    public AppointmentService(AppointmentRepository appointmentRepo, 
            AvailabilitySlotRepository availSlotRepo, 
            ServiceRepository serviceRepo) {
        this.appointmentRepo = appointmentRepo;
        this.availSlotRepo = availSlotRepo;
        this.serviceRepo = serviceRepo;
    }

    /**
     * Create new appointment
     * STEPS:
     * 1. Check if slot is exist
     * 2. Check if slot is available
     * 3. Validate service exist
     * 4. Create appointment object
     * 5. Save appoinment info
     * 6. Mark slot unavailable
     * 
     * @Transactional ensure all steps above succeed or fail together
     */
    @Transactional
    public Appointment createAppointment(
            Long customerId, Long serviceId, 
            Long slotId, String notes) {
        // 1. Check if slot exists
        AvailabilitySlot slot = availSlotRepo.findById(slotId)
            .orElseThrow(() -> new RuntimeException("Slot not found"));

        // 2. Check if slot is available
        if (!slot.isAvailable()) throw new RuntimeException("Slot is already booked.");
        
        // 3. Validate service exist
        Service service = serviceRepo.findById(serviceId)
            .orElseThrow(() -> new RuntimeException("Service not found"));
        
        /**
         * 4. Create appointment object
         * notes: ProviderId derived from slot, NOT from userInput
         */
        Appointment appointment = new Appointment();
        appointment.setCustomerId(customerId);
        appointment.setProviderId(slot.getProviderId());
        appointment.setServiceId(service.getServiceId());
        appointment.setSlotId(slot.getSlotId());
        appointment.setStatus("CONFIRMED");
        appointment.setBookedAt(LocalDateTime.now());
        appointment.setNotes(notes);

        /**
         * 5 and 6: Save info and marked slot unavailable
         * Wrapped in try-catch to handle error relate to DB
         */
        try {
            appointmentRepo.save(appointment);
            availSlotRepo.updateAvailability(slotId, false);
        }
        catch (DataIntegrityViolationException dive) {
            // this will happen if another user booked the same slot at the same time
            throw new RuntimeException("Slot is already booked");
        }
        return appointment;
    }

    /**
     * cancel an appointment 
     * STEPS:
     * 1. find appointment
     * 2. check status
     * 3. Update status to CANCELLED
     * 4. mark slot available again
     */
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        // 1. find appointment
        Appointment appointment = appointmentRepo.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Slot not found"));
        
        // 2. check status to prevent double canceling
        if ("CANCELLED".equalsIgnoreCase(appointment.getStatus())) 
                throw new RuntimeException("Appointment is already cancelled.");
        
        // 3 and 4: update appointment and mark it available
        appointmentRepo.updateStatus(appointmentId, "CANCELLED");
        availSlotRepo.updateAvailability(appointmentId, true);
    }

    //find all appointments and return it from DB
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }
}
