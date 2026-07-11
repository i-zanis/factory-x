package com.factoryx.order.application;

import com.factoryx.order.order.Order;
import com.factoryx.order.order.OrderDto;
import com.factoryx.order.order.OrderLineItem;
import com.factoryx.order.order.OrderLineItemDto;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMapper {

    public OrderDto toDto(Order order) {
        var total = order.getTotalPrice();
        return new OrderDto(
                order.getId().value(),
                order.getCustomerId().value(),
                total != null ? total.amount() : null,
                total != null ? total.currency().getCurrencyCode() : null,
                order.getLineItems().stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    private OrderLineItemDto toDto(OrderLineItem item) {
        var subtotal = item.subtotal();
        return new OrderLineItemDto(
                item.getSku().value(),
                item.getPrice().amount(),
                item.getPrice().currency().getCurrencyCode(),
                item.getQuantity().value(),
                subtotal.amount()
        );
    }
}