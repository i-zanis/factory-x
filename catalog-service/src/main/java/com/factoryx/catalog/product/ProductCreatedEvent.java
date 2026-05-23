package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;

public record ProductCreatedEvent(ProductId productId, Money initialPrice) {
    public static ProductCreatedEvent of(ProductId productId, Money initialPrice) {
        return new ProductCreatedEvent(productId, initialPrice);
    }
}
