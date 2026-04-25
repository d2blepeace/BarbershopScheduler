package edu.sjsu.cmpe172.barbershop.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

/**
 * Lightweight in-memory metrics tracker for booking operations.
 *
 * Tracks:
 *  - Total successful bookings
 *  - Total failed booking attempts
 *  - Total cancellations
 *  - Cumulative booking latency 
 */
@Service
public class BookingMetricsService {
    private final AtomicLong totalBookings = new AtomicLong(0);
    private final AtomicLong failedBookings = new AtomicLong(0);
    private final AtomicLong totalCancellations = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);

    // record successful booking with how long it took
    public void recordSuccess(long latencyMs) { 
        totalBookings.incrementAndGet();
        totalLatencyMs.addAndGet(latencyMs);
    }

    //Record failed booking attempt
    public void recordFailure() {
        failedBookings.incrementAndGet();
    }

    // Record cancellation
    public void recordCancellation() {
        totalCancellations.incrementAndGet();
    }

    //Snapshot of metrics
    public Map<String, Object> getMetrics() {
        long bookings = totalBookings.get();
        long avgLatency = bookings > 0 ? totalLatencyMs.get() / bookings : 0;
        Map<String, Object> metrics = new LinkedHashMap<>();

        metrics.put("totalBookings", bookings);
        metrics.put("failedBookings", failedBookings.get());
        metrics.put("totalCancellations", totalCancellations.get());
        metrics.put("averageBookingLatencyMs", avgLatency);

        return metrics;
    }
}
