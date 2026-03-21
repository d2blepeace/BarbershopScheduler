package edu.sjsu.cmpe172.barbershop.dto;

/**
 * Data Transfer Object for creating appointment
 * This class represents data FROM client when user books appointment
 */
public class AppointmentRequest {
    private Long customerId;
    private Long serviceId;
    private Long slotId;
    private String notes;

    //Getters and setters
    public Long getCustomerId() {return customerId;}
    public void setCustomerId(Long customerId) {this.customerId = customerId;}

    public Long getServiceId() {return serviceId;}
    public void setServiceId(Long serviceId) {this.serviceId = serviceId;}

    public Long getSlotId() {return slotId;}
    public void setSlotId(Long slotId) {this.slotId = slotId;}

    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}

    
}
