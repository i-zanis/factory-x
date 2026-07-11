package com.factoryx.order.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderDto(UUID id, UUID customerId, BigDecimal totalAmount, String currency, List<OrderLineItemDto> items) {
}