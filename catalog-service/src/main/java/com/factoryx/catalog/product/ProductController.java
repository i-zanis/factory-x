package com.factoryx.catalog.product;

import com.factoryx.catalog.api.ProductsApi;
import com.factoryx.catalog.model.Product;
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

    @Override
    public ResponseEntity<List<Product>> listProducts() {
        List<Product> products = productService.getAllProducts().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity<Product> createProduct(ProductRequest productRequest) {
        ProductEntity created = productService.createProduct(
                productRequest.getSku(),
                productRequest.getName(),
                productRequest.getPrice()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @Override
    public ResponseEntity<Product> getProductById(UUID id) {
        return productService.getProductById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Product toDto(ProductEntity entity) {
        Product dto = new Product();
        dto.setId(entity.getId());
        dto.setSku(entity.getSku());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
