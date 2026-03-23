package edu.sjsu.cmpe172.barbershop.exception;

// Throw when a booking attempt is rejected due to the slot is confirmed unavailable, not retriable

public class SlotUnavailableException extends RuntimeException {
    public SlotUnavailableException(String message) {
        super(message);
    }
}
