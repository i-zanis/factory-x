package com.factoryx.order.order;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderDtoMapper {

    public OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId().value(),
                order.getCustomerId().value(),
                order.getTotal(),
                order.getItems().stream()
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );
    }

    private OrderLineItemDto toDto(OrderLineItem item) {
        return new OrderLineItemDto(
                item.getSku(),
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}