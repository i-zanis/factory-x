package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

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

    public Money subtract(Money other) {
        validateSameCurrency(other);
        Assert.isTrue(this.amount.compareTo(other.amount()) >= 0, "Cannot subtract to negative money");
        return new Money(amount.subtract(other.amount()), currency);
    }

    public Money divide(int divisor) {
        Assert.isTrue(divisor > 0, "Divisor must be greater than zero");
        return new Money(amount.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP), currency);
    }

    public Money divide(int divisor, RoundingMode roundingMode) {
        if (divisor <= 0) throw new IllegalArgumentException("Divisor must be greater than zero");
        return new Money(amount.divide(BigDecimal.valueOf(divisor), 2, roundingMode), currency);
    }

    public double doubleValue() {
        return amount.doubleValue();
    }

    public BigDecimal ratioOf(Money other) {
        validateSameCurrency(other);
        if (other.isZero()) throw new IllegalArgumentException("Cannot divide by zero money");
        return amount.divide(other.amount(), 4, RoundingMode.HALF_UP);
    }

    @Override
    public int compareTo(Money other) {
        validateSameCurrency(other);
        return amount.compareTo(other.amount());
    }

    public boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    public boolean isLessThan(Money other) {
        return compareTo(other) < 0;
    }

    public Money discount(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        BigDecimal multiplier = BigDecimal.valueOf(100 - percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return new Money(amount.multiply(multiplier), currency);
    }

    public Money applyTax(BigDecimal percent) {
        if (percent.signum() < 0) throw new IllegalArgumentException("Tax percent cannot be negative");
        BigDecimal multiplier = BigDecimal.ONE.add(percent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return new Money(amount.multiply(multiplier), currency);
    }

    public String withSymbol() {
        return currency.getSymbol() + amount.toPlainString();
    }

    public String format(Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        format.setCurrency(currency);
        return format.format(amount);
    }

    public boolean isBetween(Money min, Money max) {
        validateSameCurrency(min);
        validateSameCurrency(max);
        return this.compareTo(min) >= 0 && this.compareTo(max) <= 0;
    }

    public boolean equalsWithinTolerance(Money other, BigDecimal tolerance) {
        validateSameCurrency(other);
        if (tolerance.signum() < 0) throw new IllegalArgumentException("Tolerance cannot be negative");
        return this.amount.subtract(other.amount()).abs().compareTo(tolerance) <= 0;
    }

    public long toMinorUnits() {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    public Money[] allocate(int parts) {
        if (parts <= 0) throw new IllegalArgumentException("Parts must be greater than zero");
        Money[] allocation = new Money[parts];
        long minorUnits = toMinorUnits();
        long base = minorUnits / parts;
        long remainder = minorUnits % parts;
        for (int i = 0; i < parts; i++) {
            allocation[i] = Money.fromMinorUnits(base + (i < remainder ? 1 : 0), currency);
        }
        return allocation;
    }

    public Money[] allocateProportionally(long[] ratios) {
        if (ratios == null || ratios.length == 0) throw new IllegalArgumentException("Ratios must not be empty");
        long totalRatio = 0;
        for (long ratio : ratios) {
            if (ratio < 0) throw new IllegalArgumentException("Ratio cannot be negative");
            totalRatio += ratio;
        }
        if (totalRatio == 0) throw new IllegalArgumentException("Total ratio must be greater than zero");

        Money[] allocation = new Money[ratios.length];
        long minorUnits = toMinorUnits();
        long remainder = minorUnits;
        for (int i = 0; i < ratios.length; i++) {
            long currentMinorUnits = (minorUnits * ratios[i]) / totalRatio;
            allocation[i] = Money.fromMinorUnits(currentMinorUnits, currency);
            remainder -= currentMinorUnits;
        }
        for (int i = 0; i < remainder; i++) {
            allocation[i] = allocation[i].add(Money.fromMinorUnits(1, currency));
        }
        return allocation;
    }

    public BigDecimal toBigDecimal() {
        return amount;
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency())) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency());
        }
    }
}
