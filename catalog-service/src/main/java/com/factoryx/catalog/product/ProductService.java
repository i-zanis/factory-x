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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductBySku(String skuValue) {
        return productRepository.findBySku(new Sku(skuValue));
    }

    @Transactional
    public Product createProduct(String skuValue, String name, double priceValue) {
        var newProduct = Product.create(new Sku(skuValue), name, Money.of(priceValue));
        return productRepository.save(newProduct);
    }
}
