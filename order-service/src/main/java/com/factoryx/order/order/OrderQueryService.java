package com.factoryx.order.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    @Cacheable(value = "order:view", key = "#customerId.value()", unless = "#result == null")
    public List<OrderSummaryProjection> getOrdersByCustomer(CustomerId customerId) {
        log.info("Redis cache miss with customerId: {}", customerId.value());
        return orderRepository.findByCustomerId(customerId);
    }
}
