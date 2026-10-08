package com.nbk.delivery_order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DeliveryOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryOrderApplication.class, args);
	}

}
