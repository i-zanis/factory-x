package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.util.StringUtils.hasText;

@Embeddable
public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Objects.requireNonNull(value, "Quantity cannot be null");
        if (value < 0) {
            throw new DomainRuleViolation("Quantity cannot be negative");
        }
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }

    public static Quantity parse(String value) {
        if (!hasText(value)) {
            throw new DomainRuleViolation("Quantity string cannot be empty");
        }
        
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
        Objects.requireNonNull(other, "Other quantity cannot be null");
        return new Quantity(this.value + other.value());
    }

    public Quantity subtract(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null");
        if (this.value < other.value()) {
            throw new DomainRuleViolation("Cannot subtract more than available quantity");
        }
        return new Quantity(this.value - other.value());
    }

    public Quantity multiply(int multiplier) {
        if (multiplier < 0) {
            throw new DomainRuleViolation("Multiplier cannot be negative");
        }
        return new Quantity(this.value * multiplier);
    }

    public Quantity divide(int divisor) {
        if (divisor <= 0) {
            throw new DomainRuleViolation("Divisor must be greater than zero");
        }
        return new Quantity(this.value / divisor);
    }

    public List<Quantity> allocate(int bins) {
        if (bins <= 0) {
            throw new DomainRuleViolation("Number of bins must be greater than zero");
        }
        var allocation = new ArrayList<Quantity>();
        int base = this.value / bins;
        int remainder = this.value % bins;
        for (int i = 0; i < bins; i++) {
            allocation.add(new Quantity(base + (i < remainder ? 1 : 0)));
        }
        return allocation;
    }

    public Quantity allocate(Quantity requested) {
        Objects.requireNonNull(requested, "Requested quantity cannot be null");
        if (requested.isLessThan(Quantity.ZERO)) {
            throw new DomainRuleViolation("Requested quantity must be positive");
        }
        return this.min(requested);
    }

    public Quantity clamp(Quantity min, Quantity max) {
        Objects.requireNonNull(min, "Min quantity cannot be null");
        Objects.requireNonNull(max, "Max quantity cannot be null");
        if (min.isGreaterThan(max)) {
            throw new DomainRuleViolation("Min quantity cannot be greater than max quantity");
        }
        if (this.isLessThan(min)) return min;
        if (this.isGreaterThan(max)) return max;
        return this;
    }

    public boolean canFulfill(Quantity requested) {
        Objects.requireNonNull(requested, "Requested quantity cannot be null");
        return this.compareTo(requested) >= 0;
    }

    @Override
    public int compareTo(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null");
        return this.value.compareTo(other.value());
    }

    public boolean isGreaterThan(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null");
        return this.compareTo(other) > 0;
    }

    public boolean isLessThan(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null");
        return this.compareTo(other) < 0;
    }

    public Quantity min(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null");
        return this.isLessThan(other) ? this : other;
    }

    public Quantity max(Quantity other) {
        Objects.requireNonNull(other, "Other quantity cannot be null");
        return this.isGreaterThan(other) ? this : other;
    }

    public long toLong() {
        return value.longValue();
    }

    public double toDouble() {
        return value.doubleValue();
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(value);
    }
}
