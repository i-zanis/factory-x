package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;

public record ProductPriceChangedEvent(ProductId productId, Money oldPrice, Money newPrice) {
    public static ProductPriceChangedEvent of(ProductId productId, Money oldPrice, Money newPrice) {
        return new ProductPriceChangedEvent(productId, oldPrice, newPrice);
    }
}
