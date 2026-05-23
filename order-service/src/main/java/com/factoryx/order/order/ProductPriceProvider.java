package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;

import java.util.List;
import java.util.Map;

/**
 * Domain Service Interface for product validation and pricing.
 */
public interface ProductPriceProvider {

    PriceInfo getPriceInfo(Sku sku);

    Map<Sku, PriceInfo> getPriceInfos(List<Sku> skus);

    record PriceInfo(Money price, boolean exists) {
        public static PriceInfo of(Money price, boolean exists) {
            return new PriceInfo(price, exists);
        }
    }
}
