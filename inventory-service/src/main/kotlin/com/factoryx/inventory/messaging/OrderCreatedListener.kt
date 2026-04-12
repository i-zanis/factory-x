package com.factoryx.inventory.messaging

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
        try {
            val event = objectMapper.readValue(message, DebeziumEvent::class.java)
            val after = event.payload.after

            // TODO(i-zanis): prob need to change to Enum.valueOf
            if ("OrderCreated" == after.type) {
                val orderData = objectMapper.readValue(after.payload, OrderCreatedDto::class.java)
                val orderId = orderData.id

                try {
                    orderData.lineItems.forEach { item ->
                        inventoryService.updateStock(item.sku, -item.quantity)
                    }
                    sendResponse(orderId, "SUCCESS")
                } catch (e: Exception) {
                    log.error("Failed to update stock for order: $orderId", e)
                    sendResponse(orderId, "FAILED")
                }
            }
        } catch (e: Exception) {
            log.error("Failed to process order created event", e)
        }
    }

    private fun sendResponse(orderId: String, status: String) {
        val response = mapOf("orderId" to orderId, "status" to status)
        kafkaTemplate.send("inventory-responses", orderId, objectMapper.writeValueAsString(response))
        log.info("Sent inventory response for order: $orderId with status: $status")
    }
}
