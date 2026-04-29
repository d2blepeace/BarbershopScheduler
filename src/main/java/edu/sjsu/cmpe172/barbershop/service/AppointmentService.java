package edu.sjsu.cmpe172.barbershop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe172.barbershop.exception.SlotUnavailableException;
import edu.sjsu.cmpe172.barbershop.model.Appointment;
import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;
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
 *  - send notification to external service after a successful booking
 *  - record metrics for monitoring
 * 
 *  Create appointment with retry for concurrency conflict
 *  Retry fires only foir slot conflcit

 * Flow: Controller -> AppointmentService -> Repo + Database -> NotificationCLient -> MockNotificationController
 */
@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepo;
    private final AvailabilitySlotRepository availSlotRepo;
    private final ServiceRepository serviceRepo;
    private final NotificationClient notificationClient;
    private final BookingMetricsService metricsService;

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    public AppointmentService(AppointmentRepository appointmentRepo, 
            AvailabilitySlotRepository availSlotRepo, 
            ServiceRepository serviceRepo,
            NotificationClient notificationClient,
            BookingMetricsService metricsService) {
        this.appointmentRepo = appointmentRepo;
        this.availSlotRepo = availSlotRepo;
        this.serviceRepo = serviceRepo;
        this.notificationClient = notificationClient;
        this.metricsService = metricsService;
    }

    /**
     * Create new appointment with retry for slot conflicts
     *
     * 1. Lock the slot row using SELECT ... FOR UPDATE                                 -CHECKED
     *    -> prevents other transactions from accessing the same slot teh same time
     * 2. Check if slot is available                                                    -CHECKED
     * 3. Validate service exist
     * 4. Claim the slot using conditional update                                       -CHECKED
     *    -> ensures only one transaction can successfully reserve the slot
     * 5. Create appointment object                                                     -CHECKED
     * 6. Save appoinment info                                                          -CHECKD
     * @Transactional ensure all steps above succeed or fail together, if fails, roll back
     * 7. Send notification to MockNotificationController    
     * 8. Record metrics           
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Appointment createAppointment(Long customerId, Long serviceId, Long slotId, String notes) {
        long startTime = System.currentTimeMillis();
        log.info("[BOOKING] Attempting booking: customerId={}, serviceId={}, slotId={}", 
                customerId, serviceId, slotId);
        
        try {
            // 1
            AvailabilitySlot slot = availSlotRepo.findByIdForUpdate(slotId)
                .orElseThrow(() -> {
                    log.error("[BOOKING] Slot {} not found in database.", slotId);
                    return new RuntimeException("Slot not found");
                });

            // 2
            if (!slot.isAvailable()) {
                log.warn("[BOOKING] Slot {} is already booked. Rejecting request for customer {}.", 
                        slotId, customerId);
                metricsService.recordFailure();
                throw new SlotUnavailableException("Slot " + slotId + " is already booked.");
            }
            
            // 3
            edu.sjsu.cmpe172.barbershop.model.Service service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> {
                    log.error("[BOOKING] Service {} not found.", serviceId);
                    return new RuntimeException("Service not found");
                });

            // 4
            int rowsUpdated = availSlotRepo.markSlotUnavailable(slotId);
            if (rowsUpdated == 0) {
                log.warn("[BOOKING] Slot {} was claimed by another transaction. Rejecting customer {}.", 
                        slotId, customerId);
                metricsService.recordFailure();
                throw new SlotUnavailableException("Slot " + slotId + " was already claimed.");
            }
            
            // 5
            Appointment appointment = new Appointment();
            appointment.setCustomerId(customerId);
            appointment.setProviderId(slot.getProviderId());
            appointment.setServiceId(service.getServiceId());
            appointment.setSlotId(slot.getSlotId());
            appointment.setStatus("CONFIRMED");
            appointment.setBookedAt(LocalDateTime.now());
            appointment.setNotes(notes);

            //6 save the appointment info
            appointmentRepo.save(appointment);

            // 7 send notification
            notificationClient.sendBookingConfirmation(appointment);

            // 8
            long latency = System.currentTimeMillis() - startTime;
            metricsService.recordSuccess(latency);
            log.info("[BOOKING] SUCCESS: customer={}, slot={}, service={}, latency={}ms", 
                    customerId, slotId, serviceId, latency);
            
            return appointment;
        }
        catch (SlotUnavailableException e) {
            throw e;
        } 
        catch (Exception e) {
            metricsService.recordFailure();
            log.error("[BOOKING] Unexpected error for customer={}, slot={}: {}", 
                    customerId, slotId, e.getMessage());
            throw e;
        }
    }

    /**
     * cancel an appointment 
     * STEPS:
     * 1. Retrieve appointment by ID                    -checked
     * 2. check status if it is not already CANCELLED   -checked
     * 3. Update status to CANCELLED                    -checked
     * 4. mark slot available again                     -checked
     */
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        log.info("[CANCEL] Cancellation requested for appointmentId={}", appointmentId);

        //1
        Appointment appointment = appointmentRepo.findById(appointmentId)
            .orElseThrow(() -> {
                log.error("[CANCEL] Appointment {} not found.", appointmentId);
                return new RuntimeException("Appointment not found");
            });
        //2
        if ("CANCELLED".equalsIgnoreCase(appointment.getStatus())) {
            log.warn("[CANCEL] Appointment {} is already cancelled. No action taken.", appointmentId);
            throw new RuntimeException("Appointment is already cancelled.");
        }
        // 3
        appointmentRepo.updateStatus(appointmentId, "CANCELLED");
        //  4
        availSlotRepo.markSlotAvailable(appointment.getSlotId());
        metricsService.recordCancellation();

        log.info("[CANCEL] SUCCESS: appointmentId={}, slotId={} released.", 
                appointmentId, appointment.getSlotId());
    }

    //find all appointments and return it from DB
    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }
}
