package com.factoryx.inventory.messaging

import com.factoryx.common.domain.Sku
import com.factoryx.inventory.stock.InventoryService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderCreatedListener(
    private val inventoryService: InventoryService,
    private val objectMapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = LoggerFactory.getLogger(OrderCreatedListener::class.java)

    @KafkaListener(topics = ["server1.public.outbox_events"], groupId = "inventory-group")
    fun handleOrderCreatedEvent(message: String) {
        val event = objectMapper.readValue(message, DebeziumEvent::class.java)
        val after = event.payload.after

        if (EventType.ORDER_CREATED.name.equals(after.type, ignoreCase = true)) {
            val orderData = objectMapper.readValue(after.payload, OrderCreatedDto::class.java)
            val orderId = orderData.id

            try {
                val updates = orderData.lineItems.map { item ->
                    Sku(item.sku) to -item.quantity
                }
                inventoryService.processOrderStockUpdate(orderId, updates)
            } catch (e: Exception) {
                log.error("Failed to process order stock update for order: $orderId", e)
                throw e // Let Kafka retry or route to DLT
            }
        }
    }
}

