package com.factoryx.catalog.product;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductSpecs {

    private ProductSpecs() {}

    public static Specification<Product> nameContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get("name").get("value")), "%" + keyword.trim().toLowerCase() + "%");
    }

    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        if (min == null && max == null) {
            return null;
        }
        return (root, query, cb) -> {
            if (min == null) {
                return cb.lessThanOrEqualTo(root.get("price").get("amount"), max);
            }
            if (max == null) {
                return cb.greaterThanOrEqualTo(root.get("price").get("amount"), min);
            }
            return cb.between(root.get("price").get("amount"), min, max);
        };
    }
}
