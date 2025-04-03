package com.quinbay.sbexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SbexampleApplication {

	private static final Logger logger = LoggerFactory.getLogger(SbexampleApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Spring Boot application...");
		SpringApplication.run(SbexampleApplication.class, args);
		logger.info("Application started successfully!");
	}

	@GetMapping("/")
	public String hello() {
		return "Hello from Docker!";
	}

}
