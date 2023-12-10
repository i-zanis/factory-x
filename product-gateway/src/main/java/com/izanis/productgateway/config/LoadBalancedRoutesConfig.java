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
            r ->
                r.path("/inventory/**", "/api/v1/inventories/**")
                    .filters(
                        f ->
                            f.circuitBreaker(
                                c ->
                                    c.setName("inventory-circuit-breaker")
                                        .setFallbackUri("forward:/inventory-failover")
                                        .setRouteId("inventory-failover")))
                    .uri("lb://localhost:8082"))
        .route(
            "product-order-service",
            r -> r.path("/order/**", "/api/v1/orders/**").uri("lb://localhost:8083"))
        .route(r -> r.path("inventory-failover/**").uri("lb://inventory-failover"))
        .build();
  }
}
