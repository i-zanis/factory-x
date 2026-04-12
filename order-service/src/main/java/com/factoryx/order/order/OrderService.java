package com.factoryx.order.order;

import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import com.factoryx.order.outbox.OutboxEventEntity;
import com.factoryx.order.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;
    private final ProductPriceProvider priceProvider;

    @Transactional
    @CircuitBreaker(name = "catalogService", fallbackMethod = "placeOrderFallback")
    public OrderEntity placeOrder(UUID customerId, List<OrderLineItemRequest> requests) {
        OrderEntity order = new OrderEntity(customerId);

        List<OrderLineItemEntity> items = requests.stream()
                .map(req -> new OrderLineItemEntity(
                        req.productId(),
                        new Sku(req.sku()),
                        new Quantity(req.quantity())
                ))
                .collect(Collectors.toList());

        order.place(items, priceProvider);

        OrderEntity savedOrder = orderRepository.save(order);


        // Push to Redis Read-Model (CQRS)
        updateRedisReadModel(savedOrder);

        try {
            OutboxEventEntity outboxEvent = new OutboxEventEntity(
                    "Order",
                    savedOrder.getId().toString(),
                    "OrderCreated",
                    objectMapper.writeValueAsString(savedOrder)
            );
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order for outbox event", e);
        }

        return savedOrder;
    }

    private void updateRedisReadModel(OrderEntity order) {
        try {
            String key = "order:view:" + order.getCustomerId();
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            log.error("Failed to update Redis read-model for order: {}", order.getId(), e);
        }
    }

    public OrderEntity placeOrderFallback(UUID customerId, List<OrderLineItemRequest> requests, Throwable t) {
        log.error("Circuit breaker 'catalogService' triggered during placeOrder for customer: {}", customerId, t);
        throw new RuntimeException("Catalog service is currently unavailable. Please try again later.", t);
    }

    // TODO(i-zanis): think if needs moving to new file
    public record OrderLineItemRequest(UUID productId, String sku, int quantity) {}
}