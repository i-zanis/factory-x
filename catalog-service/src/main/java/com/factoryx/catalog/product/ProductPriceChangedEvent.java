package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;

public record ProductPriceChangedEvent(ProductId productId, Money oldPrice, Money newPrice) {
}
