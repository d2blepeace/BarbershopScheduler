package edu.sjsu.cmpe172.barbershop.model;

import java.time.LocalDateTime;
public class AvailabilitySlot {
    private Long slotId;
    private Long providerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;

    public AvailabilitySlot() {}

    public AvailabilitySlot(Long slotId, Long providerId,
                            LocalDateTime startTime, LocalDateTime endTime, boolean isAvailable) {
        this.slotId = slotId;
        this.providerId = providerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }

    // Getter and setter
    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
