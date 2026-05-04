package com.factoryx.order.messaging;

import com.factoryx.order.order.OrderRepository;
import com.factoryx.order.order.OrderId;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
            log.info("Received inventory response for order: {}. Status: {}", new OrderId(event.orderId()), event.status());

            orderRepository.findById(new OrderId(event.orderId())).ifPresent(order -> {
                if ("SUCCESS".equals(event.status())) order.approve();
                else order.reject();
                orderRepository.save(order);
                log.info("Order: {} updated to status: {}", order.getId(), order.getStatus());
            });
        } catch (Exception e) {
            log.error("Failed to process inventory response", e);
        }
    }

    public record InventoryEvent(UUID orderId, String status) {
    }
}
