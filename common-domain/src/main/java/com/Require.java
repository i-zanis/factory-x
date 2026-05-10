package com.example.shared.domain;

import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Small domain-facing precondition utility.
 *
 * <p>Use this for generic, reusable guards inside domain objects, value objects,
 * domain services, and policies. Do not put domain-specific business rules here.</p>
 *
 * <p>Backed by Apache Commons Lang {@link Validate} where it adds value, while
 * preserving a small API that reads well in the domain model.</p>
 */
public final class Require {

    private Require() {
        throw new AssertionError("No instances of Require");
    }

    /**
     * Requires a value to be non-null.
     */
    public static <T> T nonNull(T value, String message, Object... args) {
        return Validate.notNull(value, message, args);
    }

    /**
     * Requires text to be non-null, non-empty, and not only whitespace.
     */
    public static String text(String value, String message, Object... args) {
        return Validate.notBlank(value, message, args);
    }

    /**
     * Requires a string to be non-null and non-empty. Whitespace-only values are allowed.
     */
    public static String notEmpty(String value, String message, Object... args) {
        return Validate.notEmpty(value, message, args);
    }

    /**
     * Requires a collection to be non-null and non-empty.
     */
    public static <T extends Collection<?>> T notEmpty(T value, String message, Object... args) {
        return Validate.notEmpty(value, message, args);
    }

    /**
     * Requires a map to be non-null and non-empty.
     */
    public static <T extends Map<?, ?>> T notEmpty(T value, String message, Object... args) {
        return Validate.notEmpty(value, message, args);
    }

    /**
     * Requires an array to be non-null and non-empty.
     */
    public static <T> T[] notEmpty(T[] value, String message, Object... args) {
        return Validate.notEmpty(value, message, args);
    }

    /**
     * Requires an iterable to contain no null elements.
     */
    public static <T extends Iterable<?>> T noNullElements(T value, String message, Object... args) {
        return Validate.noNullElements(value, message, args);
    }

    /**
     * Requires an array to contain no null elements.
     */
    public static <T> T[] noNullElements(T[] value, String message, Object... args) {
        return Validate.noNullElements(value, message, args);
    }

    /**
     * Requires a string length to be between min and max, inclusive.
     */
    public static String lengthBetween(String value, int min, int max, String message, Object... args) {
        text(value, message, args);
        Validate.isTrue(value.length() >= min && value.length() <= max, message, args);
        return value;
    }

    /**
     * Requires a collection size to be between min and max, inclusive.
     */
    public static <T extends Collection<?>> T sizeBetween(T value, int min, int max, String message, Object... args) {
        nonNull(value, message, args);
        Validate.isTrue(value.size() >= min && value.size() <= max, message, args);
        return value;
    }

    /**
     * Requires an integer to be positive.
     */
    public static int positive(int value, String message, Object... args) {
        Validate.isTrue(value > 0, message, args);
        return value;
    }

    /**
     * Requires a long to be positive.
     */
    public static long positive(long value, String message, Object... args) {
        Validate.isTrue(value > 0L, message, args);
        return value;
    }

    /**
     * Requires an integer to be zero or positive.
     */
    public static int nonNegative(int value, String message, Object... args) {
        Validate.isTrue(value >= 0, message, args);
        return value;
    }

    /**
     * Requires a long to be zero or positive.
     */
    public static long nonNegative(long value, String message, Object... args) {
        Validate.isTrue(value >= 0L, message, args);
        return value;
    }

    /**
     * Requires an integer to be between min and max, inclusive.
     */
    public static int betweenInclusive(int value, int min, int max, String message, Object... args) {
        Validate.inclusiveBetween(min, max, value, message, args);
        return value;
    }

    /**
     * Requires a long to be between min and max, inclusive.
     */
    public static long betweenInclusive(long value, long min, long max, String message, Object... args) {
        Validate.inclusiveBetween(min, max, value, message, args);
        return value;
    }

    /**
     * Requires a comparable value to be between min and max, inclusive.
     * Useful for domain values such as BigDecimal, LocalDate, YearMonth, etc.
     */
    public static <T extends Comparable<? super T>> T betweenInclusive(
            T value,
            T min,
            T max,
            String message,
            Object... args
    ) {
        Objects.requireNonNull(min, "min must not be null");
        Objects.requireNonNull(max, "max must not be null");
        nonNull(value, message, args);
        Validate.isTrue(value.compareTo(min) >= 0 && value.compareTo(max) <= 0, message, args);
        return value;
    }

    /**
     * Requires an integer to be between min and max, exclusive.
     */
    public static int betweenExclusive(int value, int min, int max, String message, Object... args) {
        Validate.exclusiveBetween(min, max, value, message, args);
        return value;
    }

    /**
     * Requires a long to be between min and max, exclusive.
     */
    public static long betweenExclusive(long value, long min, long max, String message, Object... args) {
        Validate.exclusiveBetween(min, max, value, message, args);
        return value;
    }

    /**
     * Requires a comparable value to be between min and max, exclusive.
     */
    public static <T extends Comparable<? super T>> T betweenExclusive(
            T value,
            T min,
            T max,
            String message,
            Object... args
    ) {
        Objects.requireNonNull(min, "min must not be null");
        Objects.requireNonNull(max, "max must not be null");
        nonNull(value, message, args);
        Validate.isTrue(value.compareTo(min) > 0 && value.compareTo(max) < 0, message, args);
        return value;
    }

    /**
     * Requires text to match a regular expression.
     */
    public static String matches(String value, String regex, String message, Object... args) {
        text(value, message, args);
        Validate.matchesPattern(value, regex, message, args);
        return value;
    }

    /**
     * Requires a value to be one of the allowed values.
     */
    public static <T> T oneOf(T value, Collection<? extends T> allowedValues, String message, Object... args) {
        Objects.requireNonNull(allowedValues, "allowedValues must not be null");
        Validate.isTrue(allowedValues.contains(value), message, args);
        return value;
    }

    /**
     * Requires a method argument or constructor argument condition to hold.
     */
    public static void argument(boolean condition, String message, Object... args) {
        Validate.isTrue(condition, message, args);
    }

    /**
     * Requires an object state condition to hold.
     */
    public static void state(boolean condition, String message, Object... args) {
        Validate.validState(condition, message, args);
    }
}
