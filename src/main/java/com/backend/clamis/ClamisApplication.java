package com.backend.clamis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClamisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClamisApplication.class, args);
	}

}
