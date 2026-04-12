package com.factoryx.common.domain;

import org.apache.commons.lang3.ObjectUtils;

public record Quantity(Integer value) {
    public Quantity {
        value = Math.max(0, ObjectUtils.defaultIfNull(value, 0));
    }
}
