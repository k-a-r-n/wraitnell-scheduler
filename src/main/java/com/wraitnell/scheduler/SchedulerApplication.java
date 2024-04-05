package com.wraitnell.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchedulerApplication {

	//TODO all create methods should return the object w/ ID

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}

}