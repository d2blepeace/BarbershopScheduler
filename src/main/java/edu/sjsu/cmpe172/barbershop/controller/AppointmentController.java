package edu.sjsu.cmpe172.barbershop.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import edu.sjsu.cmpe172.barbershop.dto.AppointmentRequest;
import edu.sjsu.cmpe172.barbershop.model.Appointment;
import edu.sjsu.cmpe172.barbershop.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.createAppointment(
            request.getCustomerId(), request.getServiceId(), request.getSlotId(), request.getNotes());
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @PutMapping("/{appointmentId}/cancel")
    public String cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return "Appointment cancelled successfully.";
    }
}
