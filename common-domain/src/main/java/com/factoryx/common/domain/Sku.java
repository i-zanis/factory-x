package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;

import static org.apache.commons.lang3.StringUtils.*;

@Embeddable
public record Sku(String value) {

    private static final String SKU_PATTERN = "^[A-Z]{3}-\\d{4}$";

    public Sku {
        Require.text(value, "SKU");
        value = normalizeSpace(value).toUpperCase();
        Require.matches(value, SKU_PATTERN, "Invalid SKU format. Expected AAA-0000");
    }

    public String category() {
        return value.substring(0, value.indexOf('-'));
    }

    public String sequence() {
        return value.substring(value.indexOf('-') + 1);
    }

    public static boolean isValid(String value) {
        if (isBlank(value)) return false;

        return normalizeSpace(value).toUpperCase().matches(SKU_PATTERN);
    }

    public String displayName() {
        return "SKU " + value;
    }

}
