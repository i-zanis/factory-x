package com.izanis.productgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local-discovery")
public class LoadBalancedRoutesConfig {
  @Bean
  public RouteLocator loadBalancedRoutes(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            "product-service",
            r -> r.path("/product/**", "/api/v1/products/**").uri("lb://localhost:8081"))
        .route(
            "product-inventory-service",
            r -> r.path("/inventory/**", "/api/v1/inventories/**").uri("lb://localhost:8082"))
        .route(
            "product-order-service",
            r -> r.path("/order/**", "/api/v1/orders/**").uri("lb://localhost:8083"))
        .build();
  }
}
