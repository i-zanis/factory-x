package com.factoryx.order.order;

import com.factoryx.order.outbox.OutboxEvent;
import com.factoryx.order.outbox.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OutboxRepository outboxRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreatedForOutbox(OrderCreatedEvent event) {
        log.info("Creating outbox event for order: {}", event.order().id());
        try {
            OutboxEvent outboxEvent = new OutboxEvent(
                    "Order",
                    event.order().id().toString(),
                    "OrderCreated",
                    objectMapper.writeValueAsString(event.order())
            );
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize order for outbox: {}", event.order().id(), e);
            throw new RuntimeException("Outbox serialization failure", e);
        }
    }

    @Async
    @EventListener
    public void handleOrderCreatedForRedis(OrderCreatedEvent event) {
        log.info("Updating Redis read-model for order: {}", event.order().id());
        try {
            String key = "order:view:" + event.order().customerId();
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(event.order()));
        } catch (JsonProcessingException e) {
            log.error("Failed to update Redis read-model for order: {}", event.order().id(), e);
        }
    }
}
