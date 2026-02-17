package com.matchjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.matchjob"})
@EnableJpaRepositories(basePackages = "com.matchjob.infrastructure.persistence")
public class MatchjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchjobApplication.class, args);
    }

}
