package com.factoryx.catalog.grpc;

import com.factoryx.catalog.product.ProductService;
import com.factoryx.common.domain.Sku;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class InternalCatalogServiceImpl extends InternalCatalogServiceGrpc.InternalCatalogServiceImplBase {

    private final ProductService productService;

    @Override
    public void getProductPrice(PriceRequest request, StreamObserver<PriceResponse> responseObserver) {
        String skuValue = request.getSku();

        productService.getProductBySku(Sku.of(skuValue))
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
}
