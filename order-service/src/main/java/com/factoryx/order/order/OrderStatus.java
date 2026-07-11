package com.factoryx.order.order;

import java.util.Set;

public enum OrderStatus {
    PENDING,
    PLACED,
    APPROVED,
    REJECTED,
    FULFILLED;

    public boolean canTransitionTo(OrderStatus target) {
        return allowedTransitions().contains(target);
    }

    private Set<OrderStatus> allowedTransitions() {
        return switch (this) {
            case PENDING   -> Set.of(PLACED);
            case PLACED    -> Set.of(APPROVED, REJECTED);
            case APPROVED  -> Set.of(FULFILLED);
            case REJECTED, FULFILLED -> Set.of();
        };
    }
}
