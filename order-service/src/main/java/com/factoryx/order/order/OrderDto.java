package com.factoryx.order.order;

import com.factoryx.common.domain.Money;

import java.util.List;
import java.util.UUID;

public record OrderDto(UUID id, UUID customerId, Money total, List<OrderLineItemDto> items) {
}