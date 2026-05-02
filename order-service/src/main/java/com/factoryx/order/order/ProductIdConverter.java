package com.factoryx.order.order;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

// TODO(i-zanis): While this provides type safety and JPA supports it via AttributeConverter,
// pragmatic use with JdbcTemplate or native SQL is terrible. We must manually unwrap (e.g. id.value())
// or create custom mapping logic everywhere outside JPA.
@Converter(autoApply = true)
public class ProductIdConverter implements AttributeConverter<ProductId, UUID> {
    @Override
    public UUID convertToDatabaseColumn(ProductId attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public ProductId convertToEntityAttribute(UUID dbData) {
        return dbData == null ? null : new ProductId(dbData);
    }
}
