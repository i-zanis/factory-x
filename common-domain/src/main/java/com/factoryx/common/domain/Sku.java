package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Collections;

@Embeddable
public record Sku(String value) {

    private static final String SKU_PATTERN = "^[A-Z]{3}-\\d{4}$";

    public Sku {
        value = StringUtils.trimToEmpty(value);

        // TODO to replace with spring validation
        if (value.isEmpty()) throw new IllegalArgumentException("SKU required");
        if (!value.matches(SKU_PATTERN)) throw new IllegalArgumentException("Invalid SKU format: " + value);
    }

    public String category() {
        return value.substring(0, value.indexOf('-'));
    }

    public String sequence() {
        return value.substring(value.indexOf('-') + 1);
    }

    public String displayName() {
        return "SKU " + value;
    }

}
