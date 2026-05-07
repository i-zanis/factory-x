package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record CustomerId(UUID value) {
    public CustomerId {
        if (value == null) throw new DomainRuleViolation("CustomerId cannot be null");
    }
    
    public static CustomerId of(UUID value) {
        return new CustomerId(value);
    }
}
