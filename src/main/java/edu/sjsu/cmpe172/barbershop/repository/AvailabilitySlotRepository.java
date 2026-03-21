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
 * - Fetch available slots from database
 * - Find slot by ID
 * - Update slot availability (true/false)
 * 
 * IMPORTANT:
 * This class ONLY interacts with the database. It does NOT contain business rules 
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
     * @param date       - date string (YYYY-MM-DD
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
     * Find a slot by its primary key (slot_id).
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
     * Update availability of a slot.
     *
     * Used in booking flow:
     * - When booking, set is_available = false
     * - When cancelling, set is_available = true
     * 
     * update() is used for: INSERT, UPDATE, DELETE
     * return number of row affected (1 row if successfully update)
     */
    public int updateAvailability(Long slotId, boolean isAvailable) {
        String sql = "UPDATE availability_slots SET is_available = ? WHERE slot_id = ?";
        return jdbcTemplate.update(sql, isAvailable, slotId);
    }
    
}
