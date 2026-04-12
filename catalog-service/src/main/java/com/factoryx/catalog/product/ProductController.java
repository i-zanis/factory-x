package com.factoryx.catalog.product;

import com.factoryx.catalog.api.ProductsApi;
import com.factoryx.catalog.model.ProductRequest;
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

    // TODO(i-zanis): see if @ResponseStatus is more idiomatic
    @Override
    public ResponseEntity<List<com.factoryx.catalog.model.Product>> listProducts() {
        var products = productService.getAllProducts().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<com.factoryx.catalog.model.Product> createProduct(ProductRequest productRequest) {
        var created = productService.createProduct(
                productRequest.getSku(),
                productRequest.getName(),
                productRequest.getPrice()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @Override
    public ResponseEntity<com.factoryx.catalog.model.Product> getProductById(UUID id) {
        return productService.getProductById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // TODO(i-zanis): maybe needs to go to another file
    private com.factoryx.catalog.model.Product toDto(Product entity) {
        com.factoryx.catalog.model.Product dto = new com.factoryx.catalog.model.Product();
        dto.setId(entity.getId());
        dto.setSku(entity.getSku().value());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice().doubleValue());
        return dto;
    }
}
