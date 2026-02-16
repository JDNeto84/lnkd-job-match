package com.matchjob.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
    "com.matchjob.application",
    "com.matchjob.core",
    "com.matchjob.infrastructure",
    "com.matchjob.bootstrap"
})
@EnableJpaRepositories(basePackages = "com.matchjob.infrastructure.persistence")
@EntityScan(basePackages = "com.matchjob.infrastructure.persistence")
public class MatchjobApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchjobApplication.class, args);
    }

}
