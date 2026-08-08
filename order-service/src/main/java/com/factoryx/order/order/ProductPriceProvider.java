package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;

import java.util.List;
import java.util.Map;

/**
 * Domain Service Interface for product validation and pricing.
 */
public interface ProductPriceProvider {

    PricedCatalogItem getPriceInfo(Sku sku);

    Map<Sku, PricedCatalogItem> getPriceInfos(List<Sku> skus);
}
