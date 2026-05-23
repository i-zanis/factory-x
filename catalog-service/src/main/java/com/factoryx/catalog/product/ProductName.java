package com.factoryx.catalog.product;

import com.factoryx.common.domain.Require;
import jakarta.persistence.Embeddable;
import static org.apache.commons.lang3.StringUtils.isAsciiPrintable;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

@Embeddable
public record ProductName(String value) {
    public ProductName {
        Require.text(value, "Product name");
        Require.argument(isAsciiPrintable(value), "Product name contains invalid characters");
        value = normalizeSpace(value);
    }

    public static ProductName of(String value) {
        return new ProductName(value);
    }
}
