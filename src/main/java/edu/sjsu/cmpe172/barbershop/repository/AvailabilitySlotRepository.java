package edu.sjsu.cmpe172.barbershop.repository;

import org.springframework.stereotype.Repository;
import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AvailabilitySlotRepository {
    public List<AvailabilitySlot> findAvailabilitySlots(Long providerId, String date) {
        List<AvailabilitySlot> slots = new ArrayList<>();
        LocalDate selectedDate = LocalDate.parse(date);

        //Mockup data
        slots.add(new AvailabilitySlot(1L, providerId, selectedDate, LocalTime.of(9, 0), true));
        slots.add(new AvailabilitySlot(2L, providerId, selectedDate, LocalTime.of(10, 0), true));
        slots.add(new AvailabilitySlot(3L, providerId, selectedDate, LocalTime.of(11, 0), true));
        slots.add(new AvailabilitySlot(4L, providerId, selectedDate, LocalTime.of(15, 0), true));
        slots.add(new AvailabilitySlot(5L, providerId, selectedDate, LocalTime.of(16, 30), true));
        slots.add(new AvailabilitySlot(6L, providerId, selectedDate, LocalTime.of(18, 0), true));

        return slots;
    }
}
