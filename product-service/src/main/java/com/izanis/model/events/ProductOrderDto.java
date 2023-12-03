package com.izanis.model.events;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record ProductOrderDto(
    UUID id,
    Integer version,
    Instant createdAt,
    Instant lastModifiedAt,
    UUID customerId,
    String customerRef,
    List<ProductOrderLineDto> productOrderLines,
    String orderStatus,
    String orderStatusUrl) {}

// TODO(i-zanis): add order status enum
