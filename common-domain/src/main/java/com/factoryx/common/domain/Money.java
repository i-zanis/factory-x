package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.requireNonNullElse;

@Embeddable
public record Money(BigDecimal amount) {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        amount = requireNonNullElse(amount, BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP);
        if (amount.signum() < 0) throw new IllegalArgumentException("Negative money forbidden");
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money of(BigDecimal value) {
        return new Money(value);
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public Money multiply(int quantity) {
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount()));
    }

    // TODO(i-zanis): do I really need this? Can it be done differently?
    public double doubleValue() {
        return amount.doubleValue();
    }
}
