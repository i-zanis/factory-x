package com.factoryx.catalog.outbox;

import com.factoryx.catalog.product.ProductCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductOutboxEventListener {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onProductCreated(ProductCreatedEvent event) {
        log.info("Saving ProductCreatedEvent to outbox for product {}", event.productId().value());
        try {
            OutboxEvent outboxEvent = new OutboxEvent(
                    "Product",
                    event.productId().value().toString(),
                    "ProductCreated",
                    objectMapper.writeValueAsString(event)
            );
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize ProductCreatedEvent for outbox", e);
            throw new RuntimeException("Outbox serialization failure for product: " + event.productId().value(), e);
        }
    }
}
