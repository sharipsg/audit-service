package com.example.auditservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.auditservice.repository")
public class AuditserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditserviceApplication.class, args);
    }

}
