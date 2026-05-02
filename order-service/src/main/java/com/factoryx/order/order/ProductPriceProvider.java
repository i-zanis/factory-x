package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;

/**
 * Domain Service Interface for product validation and pricing.
 */
public interface ProductPriceProvider {

    PriceInfo getPriceInfo(Sku sku);

    record PriceInfo(Money price, boolean exists) {
    }
}
