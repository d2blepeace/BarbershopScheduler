package edu.sjsu.cmpe172.barbershop.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import edu.sjsu.cmpe172.barbershop.model.AvailabilitySlot;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Responsibility:
 *  - Retrieve available slots for a provider
 *  - Fetch a slot by ID (normal read)
 *  - Lock a slot row for booking (for update)
 *  - Update slot availability status
 * 
 * IMPORTANT:
 * This class ONLY interact with the database. does NOT contain business rules 
 */
@Repository
public class AvailabilitySlotRepository {
    private final JdbcTemplate jdbcTemplate;

    public AvailabilitySlotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<AvailabilitySlot> slotRowMapper = (rs, rowNum) -> {
        AvailabilitySlot slot = new AvailabilitySlot();

        slot.setSlotId(rs.getLong("slot_id"));
        slot.setProviderId(rs.getLong("provider_id"));
        slot.setDate(rs.getDate("date").toLocalDate());
        slot.setTime(rs.getTime("time").toLocalTime());
        slot.setAvailable(rs.getBoolean("is_available"));
        
        return slot;
    };
    /**
     * Get all available slots for a specific provider on a given date.
     *
     * @param providerId - ID of barber/technician
     * @param date       - date string (YYYY-MM-DD()
     */
    public List<AvailabilitySlot> findAvailabilitySlots(Long providerId, String date) {
        String sql = """
                SELECT * FROM availability_slots
                WHERE provider_id = ? AND date = ? AND is_available = true
                ORDER BY time
                """;
        
        return jdbcTemplate.query(sql, slotRowMapper, providerId, Date.valueOf(date));
    }

    /**
     * Find a slot by its primary key
     *
     * This is used in booking logic to:
     * - verify the slot exists
     * - check if it's available
     */
    public Optional<AvailabilitySlot> findById(Long slotId) {
        String sql =  "SELECT * FROM availability_slots WHERE slot_id = ?";
        List<AvailabilitySlot> result = jdbcTemplate.query(sql, slotRowMapper, slotId);
        return result.stream().findFirst();
    }
    /**
     * Lock a slot row during booking to prevent race conditions
     * where two users try to book the same slot simultaneously
     */
    public Optional<AvailabilitySlot> findByIdForUpdate(Long slotId) {
        String sql = """
                SELECT * FROM availability_slots
                WHERE slot_id = ?
                FOR UPDATE 
                """;
        List<AvailabilitySlot> result = jdbcTemplate.query(sql, slotRowMapper, slotId);
        return result.stream().findFirst();
    }

    // Mark slot available again if appointment is cancelled
    public int markSlotAvailable(Long slotId) {
        String sql = """
                UPDATE availability_slots
                SET is_available = true
                WHERE slot_id = ?
                """;
        return jdbcTemplate.update(sql, slotId);
    }

    /**
     * Mark slot as unavailable ONLY if it is still available
     *
     * Returns:
     *   1: success if slot claimed
     *   0: failure if already booked by another transaction
     */
    public int markSlotUnavailable(Long slotId) {
        String sql = """
                UPDATE availability_slots
                SET is_available = false
                WHERE slot_id = ? AND is_available = true
                """;
        return jdbcTemplate.update(sql, slotId);
    }
}
