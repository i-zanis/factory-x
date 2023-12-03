package com.izanis.productservice.service.inventory.model;


import java.util.List;

public record ProductInventoryDto(List<ProductInventoryItem> items) {
}
