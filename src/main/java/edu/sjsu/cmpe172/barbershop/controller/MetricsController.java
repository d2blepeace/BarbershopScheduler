package edu.sjsu.cmpe172.barbershop.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe172.barbershop.service.BookingMetricsService;

/**
 * Exposes booking metrics for monitoring dashboards.
 */
@RestController
public class MetricsController {
    private final BookingMetricsService metricsService;

    public MetricsController(BookingMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/metrics/bookings")
    public Map<String, Object> getBookingMetrics() {
        return metricsService.getMetrics();
    }
}
