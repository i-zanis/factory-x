package com.factoryx.order.outbox;

import com.factoryx.common.domain.Require;
import jakarta.persistence.Embeddable;

@Embeddable
public record AggregateType(String value) {
    public AggregateType {
        Require.text(value, "Aggregate type");
    }

    public static AggregateType of(String value) {
        return new AggregateType(value);
    }
}
