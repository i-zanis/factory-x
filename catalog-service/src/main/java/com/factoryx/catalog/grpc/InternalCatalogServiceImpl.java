package com.factoryx.catalog.grpc;

import com.factoryx.catalog.product.ProductService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class InternalCatalogServiceImpl extends InternalCatalogServiceGrpc.InternalCatalogServiceImplBase {

    private final ProductService productService;

    @Override
    public void getProductPrice(PriceRequest request, StreamObserver<PriceResponse> responseObserver) {
        String sku = request.getSku();

        productService.getProductBySku(sku)
                .ifPresentOrElse(
                        product -> {
                            PriceResponse response = PriceResponse.newBuilder()
                                    .setSku(product.getSku())
                                    .setPrice(product.getPrice())
                                    .setExists(true)
                                    .build();
                            responseObserver.onNext(response);
                        },
                        () -> {
                            PriceResponse response = PriceResponse.newBuilder()
                                    .setSku(sku)
                                    .setPrice(0.0)
                                    .setExists(false)
                                    .build();
                            responseObserver.onNext(response);
                        }
                );
        responseObserver.onCompleted();
    }
}
