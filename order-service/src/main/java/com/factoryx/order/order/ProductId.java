package com.factoryx.order.order;

import java.util.UUID;

// TODO(i-zanis): While this provides type safety and JPA supports it via AttributeConverter,
// pragmatic use with JdbcTemplate or native SQL is terrible. We must manually unwrap (e.g. id.value())
// or create custom mapping logic everywhere outside JPA.
public record ProductId(UUID value) {
    public ProductId {
        if (value == null) throw new IllegalArgumentException("ProductId cannot be null");
    }
    
    public static ProductId of(UUID value) {
        return new ProductId(value);
    }
}
