package com.factoryx.order.infrastructure;

import com.factoryx.catalog.grpc.*;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import com.factoryx.order.order.ProductPriceProvider;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrpcProductPriceProvider implements ProductPriceProvider {

    @GrpcClient("catalog-service")
    private InternalCatalogServiceGrpc.InternalCatalogServiceBlockingStub catalogStub;

    @Override
    public PriceInfo getPriceInfo(Sku sku) {
        PriceResponse response = catalogStub.getProductPrice(
                PriceRequest.newBuilder().setSku(sku.value()).build()
        );
        return PriceInfo.of(Money.of(response.getPrice()), response.getExists());
    }

    @Override
    public Map<Sku, PriceInfo> getPriceInfos(List<Sku> skus) {
        BatchPriceRequest request = BatchPriceRequest.newBuilder()
                .addAllSkus(skus.stream().map(Sku::value).toList())
                .build();

        BatchPriceResponse response = catalogStub.getProductPrices(request);

        return response.getPricesList().stream()
                .collect(Collectors.toMap(
                        pr -> Sku.of(pr.getSku()),
                        pr -> PriceInfo.of(Money.of(pr.getPrice()), pr.getExists())
                ));
    }
}
