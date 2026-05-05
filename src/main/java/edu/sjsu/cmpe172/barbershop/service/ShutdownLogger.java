package edu.sjsu.cmpe172.barbershop.service;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Map;

import jakarta.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logs a final metrics + uptime summary when the application shuts down.
 */
@Component
public class ShutdownLogger {
    private static final Logger log = LoggerFactory.getLogger(ShutdownLogger.class);

    private final BookingMetricsService metricsService;

    public ShutdownLogger(BookingMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @PreDestroy
    public void logShutdownSummary() {
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        Duration uptime = Duration.ofMillis(uptimeMillis);
        String uptimeStr = String.format("%dd %dh %dm %ds",
                uptime.toDays(), uptime.toHoursPart(),
                uptime.toMinutesPart(), uptime.toSecondsPart());

        Map<String, Object> m = metricsService.getMetrics();
        log.info("[SHUTDOWN] uptime={} bookings={} failed={} cancellations={} avgLatencyMs={}",
                uptimeStr, m.get("totalBookings"), m.get("failedBookings"),
                m.get("totalCancellations"), m.get("averageBookingLatencyMs"));
    }
}