package edu.sjsu.cmpe172.barbershop.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represent a time slot when provider is available for booking
 */
public class AvailabilitySlot {
    private Long slotId;
    private Long providerId;
    private LocalDate date;
    private LocalTime time;
    private boolean isAvailable;

    public AvailabilitySlot() {}

    public AvailabilitySlot(Long slotId, Long providerId, LocalDate date, LocalTime time, boolean isAvailable) {
        this.slotId = slotId;
        this.providerId = providerId;
        this.date = date;
        this.time = time;
        this.isAvailable = isAvailable;
    }

    // Getter and setter
    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }

    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
