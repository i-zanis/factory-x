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
