package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;

import static java.util.Objects.requireNonNullElse;

// TODO(i-zanis): see what other methods to put here
@Embeddable
public record Quantity(Integer value) {
    public Quantity {
        value = Math.max(0, requireNonNullElse(value, 0));
    }
}
