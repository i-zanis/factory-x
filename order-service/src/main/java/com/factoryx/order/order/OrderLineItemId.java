package com.factoryx.order.order;

import com.factoryx.common.domain.Require;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineItemId implements Serializable {

    private UUID value;

    private OrderLineItemId(UUID value) {
        this.value = Require.nonNull(value, "OrderLineItemId");
    }

    public static OrderLineItemId of(UUID value) {
        return new OrderLineItemId(value);
    }

    public static OrderLineItemId generate() {
        return new OrderLineItemId(UUID.randomUUID());
    }

    public UUID value() {
        return value;
    }
}
