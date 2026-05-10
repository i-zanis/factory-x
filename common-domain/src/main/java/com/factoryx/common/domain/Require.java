package com.factoryx.common.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class Require {

    private Require() {
    }

    private static String buildMessage(String messageOrName, String defaultSuffix) {
        if (messageOrName.contains(" ")) {
            return messageOrName;
        }
        return messageOrName + defaultSuffix;
    }

    public static <T> T notNull(T value, String messageOrName) {
        if (value == null) {
            throw argumentFailure(buildMessage(messageOrName, " cannot be null"));
        }
        return value;
    }

    public static String text(String value, String messageOrName) {
        if (StringUtils.isBlank(value)) {
            throw argumentFailure(buildMessage(messageOrName, " cannot be blank"));
        }
        return value;
    }

    public static String minLength(String value, int min, String message) {
        if (min < 0) {
            throw argumentFailure(message);
        }
        notNull(value, message);
        if (value.length() < min) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static String maxLength(String value, int max, String message) {
        if (max < 0) {
            throw argumentFailure(message);
        }
        notNull(value, message);
        if (value.length() > max) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static String length(String value, int min, int max, String message) {
        if (min < 0 || max < min) {
            throw argumentFailure(message);
        }
        notNull(value, message);
        int length = value.length();
        if (length < min || length > max) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static <T extends Collection<?>> T notEmpty(T value, String messageOrName) {
        if (value == null || value.isEmpty()) {
            throw argumentFailure(buildMessage(messageOrName, " cannot be empty"));
        }
        return value;
    }

    public static <T extends Map<?, ?>> T notEmpty(T value, String messageOrName) {
        if (value == null || value.isEmpty()) {
            throw argumentFailure(buildMessage(messageOrName, " cannot be empty"));
        }
        return value;
    }

    public static <T> T[] notEmpty(T[] value, String messageOrName) {
        if (value == null || value.length == 0) {
            throw argumentFailure(buildMessage(messageOrName, " cannot be empty"));
        }
        return value;
    }

    public static <T extends Iterable<?>> T noNullElements(T value, String message) {
        if (value == null) {
            throw argumentFailure(message);
        }
        for (Object element : value) {
            if (element == null) {
                throw argumentFailure(message);
            }
        }
        return value;
    }

    public static <T> T[] noNullElements(T[] value, String message) {
        if (value == null) {
            throw argumentFailure(message);
        }
        for (T element : value) {
            if (element == null) {
                throw argumentFailure(message);
            }
        }
        return value;
    }

    public static int positive(int value, String message) {
        if (value <= 0) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static long positive(long value, String message) {
        if (value <= 0L) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static int nonNegative(int value, String message) {
        if (value < 0) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static long nonNegative(long value, String message) {
        if (value < 0L) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static int in(int value, int min, int max, String message) {
        in((long) value, (long) min, (long) max, message);
        return value;
    }

    public static long in(long value, long min, long max, String message) {
        if (max < min || value < min || value > max) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static <T extends Comparable<? super T>> T in(T value, T min, T max, String message) {
        notNull(value, message);
        notNull(min, message);
        notNull(max, message);
        if (min.compareTo(max) > 0 || value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static String matches(String value, Pattern pattern, String message) {
        notNull(value, message);
        notNull(pattern, message);
        if (!pattern.matcher(value).matches()) {
            throw argumentFailure(message);
        }
        return value;
    }

    public static String matches(String value, String regex, String message) {
        notNull(regex, message);
        try {
            return matches(value, Pattern.compile(regex), message);
        } catch (PatternSyntaxException exception) {
            throw argumentFailure(message, exception);
        }
    }

    public static <T> T oneOf(T value, Collection<? extends T> allowedValues, String message) {
        if (allowedValues == null || allowedValues.isEmpty()) {
            throw argumentFailure(message);
        }
        for (T allowedValue : allowedValues) {
            if (Objects.equals(value, allowedValue)) {
                return value;
            }
        }
        throw argumentFailure(message);
    }

    @SafeVarargs
    public static <T> T oneOf(T value, String message, T... allowedValues) {
        if (allowedValues == null || allowedValues.length == 0) {
            throw argumentFailure(message);
        }
        for (T allowedValue : allowedValues) {
            if (Objects.equals(value, allowedValue)) {
                return value;
            }
        }
        throw argumentFailure(message);
    }

    public static void argument(boolean condition, String message) {
        if (!condition) {
            throw argumentFailure(message);
        }
    }

    public static void state(boolean condition, String message) {
        if (!condition) {
            throw stateFailure(message);
        }
    }

    private static DomainRuleViolation argumentFailure(String message) {
        return new DomainRuleViolation(message);
    }

    private static DomainStateException stateFailure(String message) {
        return new DomainStateException(message);
    }

    public static class DomainPreconditionException extends RuntimeException {
        public DomainPreconditionException(String message) {
            super(message);
        }

        public DomainPreconditionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class DomainStateException extends DomainPreconditionException {
        public DomainStateException(String message) {
            super(message);
        }
    }
}
