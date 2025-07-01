package com.izanis.productinventoryfailover.model;

import java.util.List;

public record ProductInventoryDto(List<ProductInventoryItem> items) {}
