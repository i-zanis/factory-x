package com.factoryx.common.domain;

import java.util.List;
import java.util.function.Function;

public sealed interface Validation<T> {
    static <T> Validation<T> success(T value) { return new Success<>(value); }

    static <T> Validation<T> failure(String error) { return new Failure<>(List.of(error)); }

    static <T> Validation<T> failure(List<String> errors) { return new Failure<>(errors); }

    default boolean isSuccess() { return this instanceof Success; }

    default T getOrThrow() {
        if (this instanceof Success<T> s) return s.value();
        throw new IllegalStateException("Validation failed: " + ((Failure<T>) this).errors());
    }

    @SuppressWarnings("unchecked")
    default <R> Validation<R> map(Function<? super T, ? extends R> mapper) {
        if (this instanceof Success<T> s) return success(mapper.apply(s.value()));
        return (Validation<R>) this;
    }
    
    @SuppressWarnings("unchecked")
    default <R> Validation<R> flatMap(Function<? super T, Validation<R>> mapper) {
        if (this instanceof Success<T> s) return mapper.apply(s.value());
        return (Validation<R>) this;
    }

    record Success<T>(T value) implements Validation<T> {}

    record Failure<T>(List<String> errors) implements Validation<T> {}
}
