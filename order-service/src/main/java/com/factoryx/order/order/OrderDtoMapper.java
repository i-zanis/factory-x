package com.factoryx.order.order;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderDtoMapper {

    public OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId().value(),
                order.getCustomerId().value(),
                order.getTotalPrice(),
                order.getLineItems().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    private OrderLineItemDto toDto(OrderLineItem item) {
        return new OrderLineItemDto(
                item.getSku(),
                null,
                item.getPrice(),
                item.getQuantity(),
                item.subtotal()
        );
    }
}