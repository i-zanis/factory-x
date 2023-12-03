package com.izanis.model.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductOrderLineDto(
    UUID id,
    Integer version,
    Instant createdAt,
    Instant lastModifiedAt,
    String upc,
    String name,
    UUID productId,
    Integer orderQuantity,
    BigDecimal price) {}
