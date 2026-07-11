package com.factoryx.order.order;

import com.factoryx.common.domain.Require;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public record OrderLineItemId(UUID value) implements Serializable {
    public OrderLineItemId {
        Require.nonNull(value, "OrderLineItemId");
    }

    public static OrderLineItemId generate() {
        return new OrderLineItemId(UUID.randomUUID());
    }
}
