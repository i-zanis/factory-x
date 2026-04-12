package com.factoryx.common.domain;

import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {
    // TODO(i-zanis): see if there's a better name
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        amount = ObjectUtils.defaultIfNull(amount, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        if (amount.signum() < 0) throw new IllegalArgumentException("Negative money forbidden");
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public Money multiply(int quantity) {
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)));
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount()));
    }

    public double doubleValue() {
        return amount.doubleValue();
    }
}
