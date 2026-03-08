package edu.sjsu.cmpe172.barbershop.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AppointmentController {

    @PostMapping("/appointments") 
    public Map<String, Object> createAppointment(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message to user: ", "Appointment booked successfully!");
        response.put ("appointment", request);

        return response;
    }
}
