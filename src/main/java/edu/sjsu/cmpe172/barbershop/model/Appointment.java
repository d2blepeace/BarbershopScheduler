package edu.sjsu.cmpe172.barbershop.model;

import java.time.LocalDateTime;

/**
 * Represent a booking appointment of a customer:
 * Appointment connects: Customer -> Provider -> Service -> Time Slots
 * 
 * Correspond to 'appointments' in database
 */
public class Appointment {
    private Long appointmentId;
    private Long customerId;
    private Long providerId;
    private Long serviceId;
    private Long slotId;
    private String status;
    private LocalDateTime bookedAt;
    private String notes;
   
    public Appointment() {}

    public Appointment(Long appointmentId, Long customerId, Long providerId, Long serviceId, Long slotId, 
        String status, LocalDateTime bookedAt, String notes) {
            this.appointmentId = appointmentId;
            this.customerId = customerId;
            this.providerId = providerId;
            this.serviceId = serviceId;
            this.slotId = slotId;
            this.status = status;
            this.bookedAt = bookedAt;
            this.notes = notes;    
        }
    
    // Getter and setters
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) {this.appointmentId = appointmentId;}

    public Long getCustomerId() {return customerId;}
    public void setCustomerId(Long customerId) {this.customerId = customerId;}

    public Long getProviderId() {return providerId; }
    public void setProviderId(Long providerId) {this.providerId = providerId;}

    public Long getServiceId() {return serviceId;}
    public void setServiceId(Long serviceId) {this.serviceId = serviceId;}

    public Long getSlotId() {return slotId; }
    public void setSlotId(Long slotId) {this.slotId = slotId;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public LocalDateTime getBookedAt() {return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) {this.bookedAt = bookedAt;}

    public String getNotes() { return notes; }
    public void setNotes(String notes) {this.notes = notes;}
}
