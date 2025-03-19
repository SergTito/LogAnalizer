package com.example.logproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.example.logproducer"})
public class LogProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogProducerApplication.class, args);
	}

}
