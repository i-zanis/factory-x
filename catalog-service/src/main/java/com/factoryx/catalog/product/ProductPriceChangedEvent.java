package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;

import java.util.UUID;

public record ProductPriceChangedEvent(UUID productId, Money oldPrice, Money newPrice) {
}
