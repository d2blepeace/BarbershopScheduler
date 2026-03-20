package edu.sjsu.cmpe172.barbershop.repository;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import edu.sjsu.cmpe172.barbershop.model.Appointment;

/**
 * This class is responsible only for database access:
 * - inserting appointments
 * - reading appointments
 * - updating appointment status
 */
@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper to convert one SQL row into Appointment Object
    private final RowMapper<Appointment> appointmentRowMapper = (rs, rowNum) -> {
        Appointment appointment = new Appointment();

        appointment.setAppointmentId(rs.getLong("appointment_id"));
        appointment.setCustomerId(rs.getLong("customer_id"));
        appointment.setProviderId(rs.getLong("provider_id"));
        appointment.setServiceId(rs.getLong("service_id"));
        appointment.setSlotId(rs.getLong("slot_id"));
        appointment.setStatus(rs.getString("status"));

        Timestamp bookedAt = rs.getTimestamp("booked_at");

        // Prevent empty booked_at timestamp
        if (bookedAt != null) {
            appointment.setBookedAt(bookedAt.toLocalDateTime());
        }

        appointment.setNotes(rs.getString("notes"));

        return appointment;
    };

    /**
     * Save a new appointment into appointment table
     * Return:
     *  - 1 if inserted successfully
     *  - 0 if failed
     */
    public int save(Appointment appointment) {
        String sql  = """
            INSERT INTO appointments
            (customer_id, provider_id, service_id, slot_id, status, booked_at, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?)            
            """;
        return jdbcTemplate.update(
            sql, 
            appointment.getCustomerId(),
            appointment.getProviderId(),
            appointment.getServiceId(),
            appointment.getSlotId(),
            appointment.getStatus(),
            appointment.getBookedAt(),
            appointment.getNotes()
        );
    }

    /**
     * Find all appointments from database and return them
     * ORDER BY booked_at DESC: newest booking to appear first
     */
    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments ORDER BY booked_at DESC";
        return jdbcTemplate.query(sql, appointmentRowMapper);
    }
    
    /**
     * Find 1 appointment by its primary key, use Optional in case appointment is not existed
     */
    public Optional<Appointment> findById(Long appointmentId) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        List<Appointment> result = jdbcTemplate.query(sql, appointmentRowMapper, appointmentId);
        return result.stream().findFirst();
    }

    /**
     * Update status of an appointment: CONFIRMED, CANCELLED, COMPLETED.
     */
    public int updateStatus(Long appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        return jdbcTemplate.update(sql, appointmentId, status);
    }

}
