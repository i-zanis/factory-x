package com.factoryx.catalog.domain;

import java.util.UUID;

public record Product(
        UUID id,
        String sku,
        String name,
        double price,
        AuditInfo auditInfo
) {
    public static Product create(String sku, String name, double price) {
        return new Product(UUID.randomUUID(), sku, name, price, new AuditInfo(null, null, null, null));
    }
}
