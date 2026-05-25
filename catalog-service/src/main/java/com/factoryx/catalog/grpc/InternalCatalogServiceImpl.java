package com.factoryx.catalog.grpc;

import com.factoryx.catalog.product.ProductService;
import com.factoryx.common.domain.Sku;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class InternalCatalogServiceImpl extends InternalCatalogServiceGrpc.InternalCatalogServiceImplBase {

    private final ProductService productService;

    @Override
    public void getProductPrice(PriceRequest request, StreamObserver<PriceResponse> responseObserver) {
        String skuValue = request.getSku();

        productService.getProductBySku(new Sku(skuValue))
                .ifPresentOrElse(
                        product -> {
                            PriceResponse response = PriceResponse.newBuilder()
                                    .setSku(product.getSku().value())
                                    .setPrice(product.getPrice().doubleValue())
                                    .setExists(true)
                                    .build();
                            responseObserver.onNext(response);
                        },
                        () -> {
                            PriceResponse response = PriceResponse.newBuilder()
                                    .setSku(skuValue)
                                    .setPrice(0.0)
                                    .setExists(false)
                                    .build();
                            responseObserver.onNext(response);
                        }
                );
        responseObserver.onCompleted();
    }

    @Override
    public void getProductPrices(BatchPriceRequest request, StreamObserver<BatchPriceResponse> responseObserver) {
        List<Sku> skus = request.getSkusList().stream()
                .map(Sku::new)
                .toList();

        Map<String, PriceResponse> productMap = productService.getProductsBySkus(skus).stream()
                .collect(Collectors.toMap(
                        p -> p.getSku().value(),
                        p -> PriceResponse.newBuilder()
                                .setSku(p.getSku().value())
                                .setPrice(p.getPrice().doubleValue())
                                .setExists(true)
                                .build()
                ));

        List<PriceResponse> responses = request.getSkusList().stream()
                .map(sku -> productMap.getOrDefault(sku, PriceResponse.newBuilder()
                        .setSku(sku)
                        .setPrice(0.0)
                        .setExists(false)
                        .build()))
                .toList();

        responseObserver.onNext(BatchPriceResponse.newBuilder()
                .addAllPrices(responses)
                .build());
        responseObserver.onCompleted();
    }
}
