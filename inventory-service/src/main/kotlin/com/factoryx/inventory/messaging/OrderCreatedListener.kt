package com.factoryx.inventory.messaging

import com.factoryx.common.domain.DomainRuleViolation
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
                inventoryService.updateStocks(updates)
                sendResponse(orderId, "SUCCESS")
            } catch (e: DomainRuleViolation) {
                log.warn("Stock update rejected for order: $orderId - ${e.message}")
                sendResponse(orderId, "FAILED")
            }
            // TODO Consider retry or dead-letter queue for infra errors
            // Use Spring Kafka DeadLetterPublishingRecoverer for automatic DLT routing.
            // A: Yes. DLT is required to avoid losing events on transient DB failure.
            // A2: Consider @Retryable on the inventoryService call before failing completely.
        }
    }

    private fun sendResponse(orderId: String, status: String) {
        val response = mapOf("orderId" to orderId, "status" to status)
        kafkaTemplate.send("inventory-responses", orderId, objectMapper.writeValueAsString(response))
        log.info("Sent inventory response for order: $orderId with status: $status")
    }
}
