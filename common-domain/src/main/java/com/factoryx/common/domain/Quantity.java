package com.factoryx.common.domain;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Require.nonNull(value, "Quantity");
        Require.nonNegative(value, "Quantity cannot be negative");
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }

    public static Quantity parse(String value) {
        Require.text(value, "Quantity string cannot be empty");

        String cleaned = normalizeSpace(value);

        try {
            return new Quantity(Integer.parseInt(cleaned));
        } catch (NumberFormatException e) {
            throw new DomainRuleViolation("Invalid quantity format: " + cleaned);
        }
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isPositive() {
        return value > 0;
    }

    public Quantity add(Quantity other) {
        Require.nonNull(other, "Other quantity");
        return new Quantity(this.value + other.value());
    }

    public Quantity subtract(Quantity other) {
        Require.nonNull(other, "Other quantity");
        Require.argument(this.value >= other.value(), "Cannot subtract more than available quantity");
        return new Quantity(this.value - other.value());
    }

    public Quantity multiply(int multiplier) {
        Require.nonNegative(multiplier, "Multiplier cannot be negative");
        return new Quantity(this.value * multiplier);
    }

    public Quantity divide(int divisor) {
        Require.positive(divisor, "Divisor must be greater than zero");
        return new Quantity(this.value / divisor);
    }

    public List<Quantity> allocate(int bins) {
        Require.positive(bins, "Number of bins must be greater than zero");
        var allocation = new ArrayList<Quantity>();
        int base = this.value / bins;
        int remainder = this.value % bins;
        for (int i = 0; i < bins; i++) {
            allocation.add(new Quantity(base + (i < remainder ? 1 : 0)));
        }
        return allocation;
    }

    public Quantity allocate(Quantity requested) {
        Require.nonNull(requested, "Requested quantity");
        Require.argument(requested.isGreaterThan(Quantity.ZERO) || requested.isZero(), "Requested quantity must be positive");
        return this.min(requested);
    }

    public Quantity clamp(Quantity min, Quantity max) {
        Require.nonNull(min, "Min quantity");
        Require.nonNull(max, "Max quantity");
        Require.argument(!min.isGreaterThan(max), "Min quantity cannot be greater than max quantity");
        if (this.isLessThan(min)) return min;
        if (this.isGreaterThan(max)) return max;
        return this;
    }

    public boolean canFulfill(Quantity requested) {
        Require.nonNull(requested, "Requested quantity");
        return this.compareTo(requested) >= 0;
    }

    @Override
    public int compareTo(@Nonnull Quantity other) {
        Require.nonNull(other, "Other quantity");
        return this.value.compareTo(other.value());
    }

    public boolean isGreaterThan(Quantity other) {
        Require.nonNull(other, "Other quantity");
        return this.compareTo(other) > 0;
    }

    public boolean isLessThan(Quantity other) {
        Require.nonNull(other, "Other quantity");
        return this.compareTo(other) < 0;
    }

    public Quantity min(Quantity other) {
        Require.nonNull(other, "Other quantity");
        return this.isLessThan(other) ? this : other;
    }

    public Quantity max(Quantity other) {
        Require.nonNull(other, "Other quantity");
        return this.isGreaterThan(other) ? this : other;
    }

    public long toLong() {
        return value.longValue();
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(value);
    }
}