package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;

public record OrderLineItemDto(Sku sku, String name, Money price, Quantity quantity, Money subtotal) {
}