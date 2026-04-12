package com.factoryx.order.messaging;

import com.factoryx.order.order.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryResponseListener {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = {"inventory-responses"}, groupId = "order-group")
    public void handleInventoryResponse(String message) {
        try {
            InventoryEvent event = objectMapper.readValue(message, InventoryEvent.class);
            log.info("Received inventory response for order: {}. Status: {}", event.orderId(), event.status());

            orderRepository.findById(event.orderId()).ifPresent(order -> {
                if ("SUCCESS".equals(event.status())) {
                    order.complete();
                } else {
                    order.cancel();
                }
                orderRepository.save(order);
                log.info("Order: {} updated to status: {}", order.getId(), order.getStatus());
            });
        } catch (Exception e) {
            log.error("Failed to process inventory response", e);
        }
    }

    public record InventoryEvent(java.util.UUID orderId, String status) {
    }
}
