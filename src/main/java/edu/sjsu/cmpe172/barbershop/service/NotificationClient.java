package edu.sjsu.cmpe172.barbershop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.sjsu.cmpe172.barbershop.dto.NotificationRequest;
import edu.sjsu.cmpe172.barbershop.dto.NotificationResponse;
import edu.sjsu.cmpe172.barbershop.model.Appointment;

/**
 * Client that calls the mock Notification Service over HTTP.
 * It translates an internal Appointment object into a coarse-grained
 * NotificationRequest and fires a single POST to the external endpoint.
 */
@Service
public class NotificationClient {
    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);

    private final RestTemplate restTemplate;

    // Configurable via application.properties; default to localhost
    @Value("${notification.service.url:http://localhost:8080/notify}")
    private String notificationServiceUrl;

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendBookingConfirmation(Appointment appointment) {
        NotificationRequest payload = new NotificationRequest(
            appointment.getAppointmentId(),
            appointment.getCustomerId(),
            appointment.getProviderId(),
            appointment.getSlotId(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getStatus(),
            appointment.getNotes()
        );

        // try catch
        try {
            log.info("[NOTIFICATION CLIENT] Calling notification service at {}.", notificationServiceUrl);

            NotificationResponse response = restTemplate.postForObject(
                notificationServiceUrl, payload, NotificationResponse.class);
            
                if (response != null) {
                    log.info("[NOTIFICATION CLIENT] Response: {} - {}", 
                        response.getResult(), response.getMessage());
                }
        } catch (Exception e) {
            //Notification failure must not affect the booking result
            log.warn("[NOTIFICATION CLIENT] Failed to reach notification service: {}", e.getMessage());
        }
    }
}
