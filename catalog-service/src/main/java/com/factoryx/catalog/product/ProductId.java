package com.factoryx.catalog.product;

import com.factoryx.common.domain.DomainRuleViolation;
import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record ProductId(UUID value) {
    public ProductId {
        if (value == null) throw new DomainRuleViolation("ProductId cannot be null");
    }
    
    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}
