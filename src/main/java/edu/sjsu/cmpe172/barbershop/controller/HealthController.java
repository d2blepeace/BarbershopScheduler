package edu.sjsu.cmpe172.barbershop.controller;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * System health check endpoint for production monitoring.
 * Reports application status, database connectivity, and uptime.
 */
@RestController
public class HealthController {
    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * GET /health
     * Returns a JSON object with:
     *   - status: UP or DOWN
     *   - database: CONNECTED or UNREACHABLE
     *   - uptime: human-readable uptime string
     */
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new LinkedHashMap<>();
        //database connection check
        String dbStatus;
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            dbStatus = "CONNECTED";
        }
        catch (Exception e) {
            dbStatus = "UNREACHABLE";
        }

        //uptime from jvm
        long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
        Duration uptime = Duration.ofMillis(uptimeMillis);
        String uptimeStr = String.format("%dd %dh %dm %ds",
                uptime.toDays(), uptime.toHoursPart(),
                uptime.toMinutesPart(), uptime.toSecondsPart());

        health.put("status", dbStatus.equals("CONNECTED") ? "UP" : "DOWN");
        health.put("database", dbStatus);
        health.put("uptime", uptimeStr);
        return health;
    }

}
