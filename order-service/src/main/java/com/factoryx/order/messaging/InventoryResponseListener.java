package com.factoryx.order.messaging;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.order.order.Order;
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
    public void handleInventoryResponse(String message) throws Exception {
        var event = objectMapper.readValue(message, InventoryEvent.class);
        log.info("Received inventory response for order: {}. Status: {}", event.orderId().value(), event.status());

        var order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new DomainRuleViolation("Order not found: " + event.orderId().value()));
        if ("SUCCESS".equals(event.status())) order.approve();
        else order.reject();
        orderRepository.save(order);
        log.info("Order: {} updated to status: {}", order.getId().value(), order.getStatus());
    }

    public record InventoryEvent(OrderId orderId, String status) {
    }
}
