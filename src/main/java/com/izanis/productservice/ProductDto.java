package com.izanis.productservice;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record ProductDto(UUID id, Integer version, String name,
                         Product.Type type, BigDecimal price, Long upc,
                         OffsetDateTime createdOn,
                         OffsetDateTime lastModifiedOn) {
}
