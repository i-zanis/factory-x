package com.factoryx.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Order Service Entry Point
 * 
 * Handles incoming B2B and B2C orders and processes payments or fulfillment logic.
 * Reads pricing synchronously from the Catalog Service (via gRPC), saves state 
 * asynchronously via PostgreSQL/Debezium (Outbox pattern), and manages a Redis
 * CQRS view layer for quick read access.
 */
@SpringBootApplication
@EnableJpaAuditing
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
