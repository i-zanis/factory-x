package com.factoryx.common.domain;

/**
 * Fast-path exception for business rule violations.
 * Disables stack trace generation to avoid CPU/GC overhead on expected domain failures.
 */
public class DomainRuleViolation extends RuntimeException {

    public DomainRuleViolation(String message) {
        super(message, null, false, false);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
