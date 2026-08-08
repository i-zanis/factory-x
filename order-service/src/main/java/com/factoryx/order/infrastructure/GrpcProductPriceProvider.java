package com.factoryx.order.infrastructure;

import com.factoryx.catalog.grpc.*;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import com.factoryx.order.order.PricedCatalogItem;
import com.factoryx.order.order.ProductPriceProvider;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;

/**
 * gRPC client with Resilience4j Bulkhead.
 * The bulkhead pattern prevents thread exhaustion by limiting the number of concurrent 
 * synchronous calls to the catalog-service. If the catalog-service is slow, this protects 
 * the order-service from crashing due to running out of threads.
 */
@Service
@RequiredArgsConstructor
@Bulkhead(name = "catalog-grpc")
public class GrpcProductPriceProvider implements ProductPriceProvider {

    @GrpcClient("catalog-service")
    private InternalCatalogServiceGrpc.InternalCatalogServiceBlockingStub catalogStub;

    @Override
    public PricedCatalogItem getPriceInfo(Sku sku) {
        PriceResponse response = catalogStub.getProductPrice(
                PriceRequest.newBuilder().setSku(sku.value()).build()
        );
        return new PricedCatalogItem(sku, new Money(response.getPrice()), response.getExists());
    }

    @Override
    public Map<Sku, PricedCatalogItem> getPriceInfos(List<Sku> skus) {
        BatchPriceRequest request = BatchPriceRequest.newBuilder()
                .addAllSkus(skus.stream().map(Sku::value).toList())
                .build();

        BatchPriceResponse response = catalogStub.getProductPrices(request);

        return response.getPricesList().stream()
                .collect(Collectors.toMap(
                        pr -> new Sku(pr.getSku()),
                        pr -> new PricedCatalogItem(new Sku(pr.getSku()), new Money(pr.getPrice()), pr.getExists())
                ));
    }
}
