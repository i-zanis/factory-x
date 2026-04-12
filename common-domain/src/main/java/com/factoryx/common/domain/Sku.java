package com.factoryx.common.domain;

import org.apache.commons.lang3.StringUtils;

public record Sku(String value) {

    private static final String SKU_PATTERN = "^[A-Z]{3}-\\d{4}$";

    public Sku {
        value = StringUtils.trimToEmpty(value);

        if (value.isEmpty()) throw new IllegalArgumentException("SKU required");
        if (!value.matches(SKU_PATTERN)) throw new IllegalArgumentException("Invalid SKU format: " + value);
    }
}
