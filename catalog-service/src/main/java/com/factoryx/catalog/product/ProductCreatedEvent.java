package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;

public record ProductCreatedEvent(ProductId productId, Money initialPrice) {
}
