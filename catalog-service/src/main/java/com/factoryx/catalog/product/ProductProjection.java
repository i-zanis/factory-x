package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;

import java.util.UUID;

public interface ProductProjection {
    UUID getId();
    Sku getSku();
    String getName();
    Money getPrice();
}