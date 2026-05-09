package com.factoryx.catalog.product;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import java.util.Currency;
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

    public Optional<Product> getProductBySku(String skuValue) throws Domain{
        return productRepository.findBySku(new Sku(skuValue));
    }

    @Transactional
    public Product createProduct(String skuValue, String name, double priceValue) {
        Sku sku = new Sku(skuValue);
        Money price = Money.of(java.math.BigDecimal.valueOf(priceValue));
        Product product = Product.create(sku, name, price);
        return productRepository.save(product);
    }
}
