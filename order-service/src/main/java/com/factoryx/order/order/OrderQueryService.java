package com.factoryx.order.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    @Cacheable(value = "order:view", key = "#customerId", unless = "#result == null")
    public List<OrderSummaryProjection> getOrdersByCustomer(UUID customerId) {
        log.info("Redis cache miss with customerId: {}", customerId);
        return orderRepository.findByCustomerId(new CustomerId(customerId));
    }
}
