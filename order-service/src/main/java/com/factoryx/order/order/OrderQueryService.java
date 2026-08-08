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

    private final OrderSummaryViewRepository readModelRepository;

    public List<OrderSummaryView> getOrdersByCustomer(CustomerId customerId) {
        log.info("Querying relational read model for customerId: {}", customerId.value());
        return readModelRepository.findByCustomerId(customerId.value());
    }
}
