package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(OrderInfo order) {

    public OrderCreatedEvent(Order order) {
        this(new OrderInfo(
                order.getId().value(),
                order.getCustomerId().value(),
                order.getTotalPrice(),
                order.getLineItems().stream()
                        .map(item -> new OrderLineItemInfo(
                                item.getProductId().value(),
                                item.getSku(),
                                item.getQuantity(),
                                item.getPrice()
                        ))
                        .toList()
        ));
    }

    public record OrderInfo(
            UUID id,
            UUID customerId,
            Money totalPrice,
            List<OrderLineItemInfo> lineItems
    ) {
    }

    public record OrderLineItemInfo(
            UUID productId,
            Sku sku,
            Quantity quantity,
            Money price
    ) {
    }
}
