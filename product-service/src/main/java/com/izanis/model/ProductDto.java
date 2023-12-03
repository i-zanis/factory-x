package com.izanis.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.izanis.productservice.entity.Product;
import lombok.Builder;

@Builder(toBuilder = true, builderClassName = "Builder")
public record ProductDto(
    UUID id,
    String name,
    BigDecimal price,
    Long upc,
    Integer totalInventory,
    Product.Category category,
    Integer availableInventory) {}
