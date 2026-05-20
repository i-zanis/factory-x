package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductProjection> getAllProducts() {
        return productRepository.findAllProjectedBy();
    }

    public Optional<Product> getProductById(ProductId id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(Sku sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional
    public Product createProduct(Sku sku, String name, Money price) {
        Product product = Product.create(sku, name, price);
        return productRepository.save(product);
    }
}