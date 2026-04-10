package edu.sjsu.cmpe172.barbershop.dto;

/**
 * Data transfer object that sent to MockNotificationController
 * Coarse-grained: one object per booking event instead of many fine-grained calls.
 */
public class NotificationRequest {
    private Long appointmentId;
    private Long customerId;
    private Long providerId;
    private Long slotId;
    private String status;
    private String notes;

    public NotificationRequest() {}

    public NotificationRequest(Long appointmentId, Long customerId, Long providerId, Long slotId,
                                String status, String notes) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.providerId = providerId;
        this.slotId = slotId;
        this.status = status;
        this.notes = notes;
    }

    //Getter and setter

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

