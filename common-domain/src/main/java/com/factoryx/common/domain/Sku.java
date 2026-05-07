package com.factoryx.common.domain;

import jakarta.persistence.Embeddable;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.springframework.util.StringUtils.hasText;

@Embeddable
public record Sku(String value) {

    private static final String SKU_PATTERN = "^[A-Z]{3}-\\d{4}$";

    public Sku {
        if (!hasText(value)) {
            throw new DomainRuleViolation("SKU is required");
        }
        value = normalizeSpace(value).toUpperCase();
        if (!value.matches(SKU_PATTERN)) {
            throw new DomainRuleViolation("Invalid SKU format. Expected AAA-0000");    
        }
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
