package edu.sjsu.cmpe172.barbershop.exception;

// If transient concurrecncy fail = retry to maximum attempts

public class SlotConflictException extends RuntimeException {
    public SlotConflictException(String message) {
        super(message);
    }
}
