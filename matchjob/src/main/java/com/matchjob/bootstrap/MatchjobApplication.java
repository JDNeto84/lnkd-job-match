package com.matchjob.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.matchjob")
public class MatchjobApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchjobApplication.class, args);
	}

}
