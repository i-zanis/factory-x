package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;

import static java.util.Objects.requireNonNullElse;

// TODO(i-zanis): see what other methods to put here
@Embeddable
public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        value = Math.max(0, requireNonNullElse(value, 0));
    }
}
