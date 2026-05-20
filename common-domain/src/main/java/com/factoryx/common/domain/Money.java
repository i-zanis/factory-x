package com.factoryx.common.domain;

import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Currency;
import java.util.Locale;

public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {

    public static final Money ZERO = new Money(BigDecimal.ZERO, Currency.getInstance("USD"));

    public Money {
        Require.nonNull(amount, "Amount");
        Require.nonNull(currency, "Currency");
        Require.nonNegative(amount.signum(), "Negative money forbidden");
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value), Currency.getInstance("USD"));
    }

    public static Money of(BigDecimal value) {
        return new Money(value, Currency.getInstance("USD"));
    }

    public static Money of(BigDecimal value, Currency currency) {
        return new Money(value, currency);
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public Money multiply(int quantity) {
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }

    public static Money fromMinorUnits(long minorUnits, Currency currency) {
        return new Money(BigDecimal.valueOf(minorUnits).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP), currency);
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(amount.add(other.amount()), currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        Require.argument(this.amount.compareTo(other.amount()) >= 0, "Cannot subtract to negative money");
        return new Money(amount.subtract(other.amount()), currency);
    }

    public Money divide(int divisor) {
        Require.positive(divisor, "Divisor must be greater than zero");
        return new Money(amount.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP), currency);
    }

    public double doubleValue() {
        return amount.doubleValue();
    }

    public Money divide(int divisor, RoundingMode roundingMode) {
        Require.positive(divisor, "Divisor must be greater than zero");
        return new Money(amount.divide(BigDecimal.valueOf(divisor), 2, roundingMode), currency);
    }

    @Override
    public int compareTo(@Nonnull Money other) {
        validateSameCurrency(other);
        return amount.compareTo(other.amount());
    }

    public boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    public boolean isLessThan(Money other) {
        return compareTo(other) < 0;
    }

    public BigDecimal ratioOf(Money other) {
        validateSameCurrency(other);
        Require.argument(!other.isZero(), "Cannot divide by zero money");
        return amount.divide(other.amount(), 4, RoundingMode.HALF_UP);
    }

    public Money discount(int percentage) {
        Require.in(percentage, 0, 100, "Discount percentage must be between 0 and 100");
        BigDecimal multiplier = BigDecimal.valueOf(100 - percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
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

    public boolean in(Money min, Money max) {
        validateSameCurrency(min);
        validateSameCurrency(max);
        return this.compareTo(min) >= 0 && this.compareTo(max) <= 0;
    }

    public Money applyTax(BigDecimal percent) {
        Require.nonNegative(percent.signum(), "Tax percent cannot be negative");
        BigDecimal multiplier = BigDecimal.ONE.add(percent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return new Money(amount.multiply(multiplier), currency);
    }

    public long toMinorUnits() {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    public boolean equalsWithinTolerance(Money other, BigDecimal tolerance) {
        validateSameCurrency(other);
        Require.nonNegative(tolerance.signum(), "Tolerance cannot be negative");
        return this.amount.subtract(other.amount()).abs().compareTo(tolerance) <= 0;
    }

    public List<Money> allocate(int parts) {
        Require.positive(parts, "Parts must be greater than zero");
        List<Money> allocation = new ArrayList<>(parts);
        long minorUnits = toMinorUnits();
        long base = minorUnits / parts;
        long remainder = minorUnits % parts;
        for (int i = 0; i < parts; i++) {
            allocation.add(Money.fromMinorUnits(base + (i < remainder ? 1 : 0), currency));
        }

        return allocation;
    }

    public List<Money> allocateProportionally(List<Long> ratios) {
        Require.notEmpty(ratios, "Ratios must not be empty");
        long totalRatio = 0;
        for (long ratio : ratios) {
            Require.nonNegative(ratio, "Ratio cannot be negative");
            totalRatio += ratio;
        }
        Require.argument(totalRatio > 0, "Total ratio must be greater than zero");

        List<Money> allocation = new ArrayList<>(ratios.size());
        long minorUnits = toMinorUnits();
        long remainder = minorUnits;
        for (var ratio : ratios) {
            long currentMinorUnits = (minorUnits * ratio) / totalRatio;
            allocation.add(Money.fromMinorUnits(currentMinorUnits, currency));
            remainder -= currentMinorUnits;
        }
        for (int i = 0; i < remainder; i++) {
            allocation.set(i, allocation.get(i).add(Money.fromMinorUnits(1, currency)));
        }
        return allocation;
    }

    public BigDecimal toBigDecimal() {
        return amount;
    }

    private void validateSameCurrency(Money other) {
        Require.argument(this.currency.equals(other.currency()), "Currency mismatch: " + this.currency + " vs " + other.currency());
    }
}