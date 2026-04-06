package com.factoryx.catalog.service;

import com.factoryx.catalog.persistence.ProductEntity;
import com.factoryx.catalog.persistence.ProductRepository;
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

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductEntity> getProductById(UUID id) {
        return productRepository.findById(id);
    }
    
    public Optional<ProductEntity> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional
    public ProductEntity createProduct(String sku, String name, double price) {
        ProductEntity newProduct = new ProductEntity(UUID.randomUUID(), sku, name, price);
        return productRepository.save(newProduct);
    }
}
