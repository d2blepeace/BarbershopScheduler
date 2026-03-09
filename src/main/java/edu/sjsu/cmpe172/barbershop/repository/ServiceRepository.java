package edu.sjsu.cmpe172.barbershop.repository;

import edu.sjsu.cmpe172.barbershop.model.Service;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 * Repository layer responsible for getting salon service
 * 
 * This class would query SQL database using JDBC 
 * 
 * Flow: Controller -> Service -> Repo -> Database
 */
@Repository
public class ServiceRepository {

    /**
     * Return all service offered by a salon
     * In the future, this will execute SQL, currently it is just mockup data
     * @return
     */
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();

        services.add(new Service(1L, "Haircut", 30, 35.0, "Hair"));
        services.add(new Service(2L, "Women Haircut & Styling", 55, 50.0, "Hair"));
        services.add(new Service(3L, "Women Haircut & Color", 120, 150.0, "Hair"));
        services.add(new Service(4L, "Men haircut & Shaving", 35, 50.0, "Hair"));
        services.add(new Service(5L, "Haircut & Beard trim", 60, 70.0, "Hair"));
        services.add(new Service(6L, "Kid haircut", 30, 20.0, "Hair"));
        services.add(new Service(7L, "Express Nail Service", 30, 50.0, "Nail"));
        services.add(new Service(8L, "Manicure & Color", 60, 100.0, "Nail"));

        return services;
    }
}
