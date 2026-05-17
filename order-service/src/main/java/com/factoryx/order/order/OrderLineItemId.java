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

    public OrderLineItemId(UUID value) {
        this.value = Require.nonNull(value, "OrderLineItemId");
    }

    public UUID value() {
        return value;
    }
}
