package edu.sjsu.cmpe172.barbershop.repository;

import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe172.barbershop.model.Provider;

@Repository
public class ProviderRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProviderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @NonNull
    private final RowMapper<Provider> providerRowMapper = (rs, rowNum) -> {
        Provider p = new Provider();
        p.setProviderId(rs.getLong("provider_id"));
        p.setName(rs.getString("name"));
        p.setBio(rs.getString("bio"));
        p.setAvatarUrl(rs.getString("avatar_url"));
        p.setActive(rs.getBoolean("is_active"));
        return p;
    };

    public List<Provider> findAll() {
        String sql = "SELECT * FROM providers ORDER BY provider_id";
        return jdbcTemplate.query(sql, providerRowMapper);
    }
}
