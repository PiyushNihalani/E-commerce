package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CommonProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonProjectApplication.class, args);
	}

}
