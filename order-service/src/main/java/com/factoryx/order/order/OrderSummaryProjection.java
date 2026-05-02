package com.factoryx.order.order;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderSummaryProjection {
    String getOrderId();
    String getStatus();
    BigDecimal getTotalPriceAmount();
    String getTotalPriceCurrency();
    Instant getCreatedAt();
    String getProductId();
    String getSku();
    Integer getQuantity();
    BigDecimal getPriceAmount();
    String getPriceCurrency();
}