package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record OrderId(UUID value) {
    public OrderId {
        if (value == null) throw new DomainRuleViolation("OrderId cannot be null");
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}
