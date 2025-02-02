package com.backend.farmon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FarmonApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmonApplication.class, args);
	}

}
