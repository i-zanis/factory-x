package com.factoryx.catalog.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductUseCase {

    private final ProductRepository productRepository;

    public ProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Product createProduct(String sku, String name, double price) {
        Product newProduct = Product.create(sku, name, price);
        return productRepository.save(newProduct);
    }
}
