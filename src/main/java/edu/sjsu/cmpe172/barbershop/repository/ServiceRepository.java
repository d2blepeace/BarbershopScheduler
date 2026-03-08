package edu.sjsu.cmpe172.barbershop.repository;

import edu.sjsu.cmpe172.barbershop.model.Service;
import org.springframework.stereotype.Repository;
import java.util.*;

public class ServiceRepository {
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();

        services.add(new Service(1L, "Haircut", 30, 35.0));

    }
}
