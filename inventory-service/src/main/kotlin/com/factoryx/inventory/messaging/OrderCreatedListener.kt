package com.factoryx.inventory.messaging

import com.factoryx.inventory.stock.InventoryService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderCreatedListener(
    private val inventoryService: InventoryService,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(topics = ["server1.public.outbox_events"], groupId = "inventory-group")
    fun handleOrderCreatedEvent(message: String) {
        try {
            val jsonNode = objectMapper.readTree(message)
            val payload = jsonNode.get("payload")

            // TODO(i-zanis): to replace with more Kotlin way here
            // Extract the 'after' state from Debezium payload
            if (payload != null && payload.has("after")) {
                val after = payload.get("after")
                val eventType = after.get("type").asText()

                // TODO(i-zanis): prob need to change to Enum.valueOf
                if ("OrderCreated" == eventType) {
                    val payloadData = after.get("payload").asText()
                    val orderData = objectMapper.readTree(payloadData)
                    val items = orderData.get("lineItems")

                    items.forEach { item ->
                        val sku = item.get("sku").asText()
                        val quantity = item.get("quantity").asInt()
                        inventoryService.updateStock(sku, -quantity)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
