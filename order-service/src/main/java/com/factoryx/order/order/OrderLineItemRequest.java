package com.factoryx.order.order;

import java.util.UUID;

public record OrderLineItemRequest(UUID productId, String sku, int quantity) {}
