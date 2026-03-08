package edu.sjsu.cmpe172.barbershop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BarbershopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarbershopApplication.class, args);
	}

}
