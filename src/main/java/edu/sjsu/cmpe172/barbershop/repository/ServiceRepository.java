package edu.sjsu.cmpe172.barbershop.repository;

import edu.sjsu.cmpe172.barbershop.model.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Rsponsibility:
 *  - Retrieve service data from database
 *  - Provide basic queries: findAll, findById
 * 
 * IMPORTANT: 
 *  - this class only handle interaction to database
 *  - DOES NOT validate business rule or if a service can be booked
 */
@Repository
public class ServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    private ServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Service> servicRowMapper = (rs, rowNum) -> {
        Service service = new Service();

        service.setServiceId(rs.getLong("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setDuration(rs.getInt("duration"));
        service.setPrice(rs.getDouble("price"));
        service.setType(rs.getString("type"));

        return service;
    };

    // get all services offered by salon
    public List<Service> findAll() {
        String sql = "SELECT * FROM services ORDER BY service_id";
        return jdbcTemplate.query(sql, servicRowMapper);
    }

    /**
     * find a service by ID to validate that service exists
     * @param serviceId
     * @return empty if service not found
     */
    public Optional<Service> findById(Long serviceId) {
        String sql = "SELECT * FROM services WHERE serivce_id = ?";
        List<Service> result = jdbcTemplate.query(sql, servicRowMapper, serviceId);
        return result.stream().findFirst();
    }
}
