package com.factoryx.order.outbox;

import com.factoryx.common.domain.Require;
import jakarta.persistence.Embeddable;

@Embeddable
public record EventType(String value) {
    public EventType {
        Require.text(value, "Event type");
    }
}
