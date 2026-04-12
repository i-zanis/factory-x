package com.factoryx.inventory.messaging

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DebeziumEvent(
    val payload: DebeziumPayload
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class DebeziumPayload(
    val after: OutboxEventPayload
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OutboxEventPayload(
    val type: String,
    val payload: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderCreatedDto(
    val id: String,
    val lineItems: List<OrderItemDto>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderItemDto(
    val sku: String,
    val quantity: Int
)
