package com.factoryx.catalog.product;

import com.factoryx.catalog.api.ProductsApi;
import com.factoryx.catalog.model.ProductRequest;
import com.factoryx.catalog.model.Product;
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
    private final ProductQueryService productQueryService;

    @Override
    public ResponseEntity<List<Product>> listProducts() {
        var products = productQueryService.getAllProducts().stream()
                .map(ProductAssembler::toDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<Product> createProduct(ProductRequest productRequest) {
        Sku sku = new Sku(productRequest.getSku());
        Money price = new Money(productRequest.getPrice());
        
        var created = productService.createProduct(sku, productRequest.getName(), price);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductAssembler.toDto(created));
    }

    @Override
    public ResponseEntity<Product> getProductById(UUID id) {
        return productQueryService.getProductById(new ProductId(id))
                .map(ProductAssembler::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
