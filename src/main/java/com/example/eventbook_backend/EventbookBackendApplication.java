package com.example.eventbook_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventbookBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventbookBackendApplication.class, args);
	}

}
