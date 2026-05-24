package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;

import java.util.List;

public record OrderCreatedEvent(OrderId orderId, CustomerId customerId, Money totalPrice, List<OrderLineItemInfo> lineItems) {
    public record OrderLineItemInfo(ProductId productId, Sku sku, Quantity quantity, Money price) {
    }
}
