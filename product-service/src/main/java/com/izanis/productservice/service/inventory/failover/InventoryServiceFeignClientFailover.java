package com.izanis.productservice.service.inventory.failover;

import com.izanis.productservice.service.inventory.InventoryServiceFeignClient;
import com.izanis.productservice.service.inventory.failover.InventoryFailoverFeignClient;
import com.izanis.productservice.service.inventory.model.ProductInventoryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryServiceFeignClientFailover implements InventoryServiceFeignClient {
    private final InventoryFailoverFeignClient client;

    @Override
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallback")
    public Optional<ProductInventoryDto> getProductInventory(List<UUID> productId) {
        return client.getProductInventory();
    }

    public Optional<ProductInventoryDto> fallback(List<UUID> productId, Throwable t) {
        // Implement your fallback logic here
        return Optional.empty();
    }
}