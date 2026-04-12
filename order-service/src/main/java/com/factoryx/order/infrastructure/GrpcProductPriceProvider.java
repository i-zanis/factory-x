package com.factoryx.order.infrastructure;

import com.factoryx.catalog.grpc.InternalCatalogServiceGrpc;
import com.factoryx.catalog.grpc.PriceRequest;
import com.factoryx.catalog.grpc.PriceResponse;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import com.factoryx.order.order.ProductPriceProvider;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

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
        return new PriceInfo(Money.of(response.getPrice()), response.getExists());
    }

}
