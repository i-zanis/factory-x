package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.order.outbox.AggregateType;
import com.factoryx.order.outbox.EventType;
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
        log.info("Creating outbox event for order: {}", event.orderId().value());
        try {
            OutboxEvent outboxEvent = OutboxEvent.from(
                    AggregateType.of("Order"),
                    event.orderId().value().toString(),
                    EventType.of("OrderCreated"),
                    objectMapper.writeValueAsString(event)
            );
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize order for outbox: {}", event.orderId().value(), e);
            throw new DomainRuleViolation("Outbox serialization failure", e);
        }
    }

    @Async
    @EventListener
    public void handleOrderCreatedForRedis(OrderCreatedEvent event) {
        log.info("Updating Redis read-model for order: {}", event.orderId().value());
        try {
            String key = "order:view:" + event.customerId().value();
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            log.error("Failed to update Redis read-model for order: {}", event.orderId().value(), e);
        }
    }
}
