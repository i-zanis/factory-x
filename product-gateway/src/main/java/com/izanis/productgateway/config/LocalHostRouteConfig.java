package com.izanis.productgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local-discovery")
public class LocalHostRouteConfig {
  @Bean
  public RouteLocator loadHostRoutes(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            "product-service",
            r ->
                r.path("/api/v1/product*", "/api/v1/product/**", "api/v1/upc/*")
                    .uri("http://localhost:8080"))
        .route(
            "product-inventory-service",
            r -> r.path("/api/v1/inventory*", "/api/v1/inventory/**").uri("http://localhost:8081"))
        .route(
            "product-order-service",
            r -> r.path("/api/v1/order*", "/api/v1/order/**").uri("http://localhost:8082"))
        .build();
  }
}
