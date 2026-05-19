package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;
import java.util.UUID;

public record ProductCreatedEvent(UUID productId, Money initialPrice) {
}
