package com.factoryx.order.order;

import java.math.BigDecimal;

public record OrderLineItemDto(String sku, BigDecimal price, String currency, int quantity, BigDecimal subtotal) {
}