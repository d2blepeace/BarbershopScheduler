package edu.sjsu.cmpe172.barbershop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application-wide bean configuration.
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate bean used by NotificationClient to call the mock external service
     * Declared here so it can be injected and easily replaced with a mock in tests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}