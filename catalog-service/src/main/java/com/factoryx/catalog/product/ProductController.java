package com.factoryx.catalog.product;

import com.factoryx.catalog.api.ProductsApi;
import com.factoryx.catalog.model.ProductRequest;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<List<com.factoryx.catalog.model.Product>> listProducts() {
        var products = productService.getAllProducts().stream()
                .map(ProductAssembler::toDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<com.factoryx.catalog.model.Product> createProduct(ProductRequest productRequest) {
        Sku sku = Sku.of(productRequest.getSku());
        // TODO what kind of error this will throw now? will be be service level error with domain exception? is it correct?
        Money price = Money.of(productRequest.getPrice());
        
        var created = productService.createProduct(sku, productRequest.getName(), price);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductAssembler.toDto(created));
    }

    @Override
    public ResponseEntity<com.factoryx.catalog.model.Product> getProductById(UUID id) {
        return productService.getProductById(ProductId.of(id))
                .map(ProductAssembler::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
