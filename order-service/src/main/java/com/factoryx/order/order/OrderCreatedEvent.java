package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        Money totalPrice,
        List<OrderLineItemInfo> lineItems
) {
    public record OrderLineItemInfo(
            UUID productId,
            Sku sku,
            Quantity quantity,
            Money price
    ) {
    }
}
