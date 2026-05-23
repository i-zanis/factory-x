package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        OrderId orderId,
        CustomerId customerId,
        Money totalPrice,
        List<OrderLineItemInfo> lineItems
) {
    public static OrderCreatedEvent of(OrderId orderId, CustomerId customerId, Money totalPrice, List<OrderLineItemInfo> lineItems) {
        return new OrderCreatedEvent(orderId, customerId, totalPrice, lineItems);
    }

    public record OrderLineItemInfo(
            ProductId productId,
            Sku sku,
            Quantity quantity,
            Money price
    ) {
        public static OrderLineItemInfo of(ProductId productId, Sku sku, Quantity quantity, Money price) {
            return new OrderLineItemInfo(productId, sku, quantity, price);
        }
    }
}
