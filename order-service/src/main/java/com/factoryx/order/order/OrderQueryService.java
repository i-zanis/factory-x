package com.factoryx.order.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public Object getOrdersByCustomer(UUID customerId) {
        String key = "order:view:" + customerId;
        try {
            String cachedOrder = redisTemplate.opsForValue().get(key);
            if (cachedOrder != null) {
                log.info("Redis cache hit for customer: {}", customerId);
                return objectMapper.readTree(cachedOrder);
            }
        } catch (Exception e) {
            log.error("Redis error, falling back to Postgres for customer: {}", customerId, e);
        }

        log.info("Redis cache miss or error, querying Postgres for customer: {}", customerId);
        return orderRepository.findByCustomerId(customerId);
    }
}
