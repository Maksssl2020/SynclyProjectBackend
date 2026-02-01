package com.synclyplatform.synclyprojectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.synclyplatform.synclyprojectbackend.repository")
public class SynclyProjectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynclyProjectBackendApplication.class, args);
    }

}
