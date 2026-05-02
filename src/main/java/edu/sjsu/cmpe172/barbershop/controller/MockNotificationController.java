package edu.sjsu.cmpe172.barbershop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.sjsu.cmpe172.barbershop.dto.NotificationRequest;
import edu.sjsu.cmpe172.barbershop.dto.NotificationResponse;

/**
 * Mocking external notification service
 *  This controller will pretend to be an external service of notification provider.
 * 
 * Distribution boundary:
 *  AppointmentService -> NotificationClient --http POST /notify--> MockNotificationController
 */
@RestController
@RequestMapping("/notify")
public class MockNotificationController {
    private static final Logger log = LoggerFactory.getLogger(MockNotificationController.class);

    /**
     * Receive a booking confirm noti
     * Simulate sending email/msg to customer 
     * @param request - full appointment details in one coarse-grained payload
     * @return NotificationResponse that confirm the notification has been proceed
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest request) {
        
        log.info("[MOCK NOTIFICATION SERVICE] Received booking information:");
        log.info("  Appointment ID  : {}", request.getAppointmentId());
        log.info("  Customer ID     : {}", request.getCustomerId());
        log.info("  Provider ID     : {}", request.getProviderId());
        log.info("  Slot ID         : {}", request.getSlotId());
        log.info("  Date            : {}", request.getAppointmentDate());
        log.info("  Time            : {}", request.getAppointmentTime());
        log.info("  Status          : {}", request.getStatus());
        log.info("  Notes           : {}", request.getNotes());
        log.info("[MOCK NOTIFICATION SERVICE] Confirmation sent to customer {}.", request.getCustomerId());

        NotificationResponse response = new NotificationResponse(
            "SUCCESS", "Confirmation notification sent to customer." +
            request.getCustomerId()
        );

        return ResponseEntity.ok(response);
    }
    
}
