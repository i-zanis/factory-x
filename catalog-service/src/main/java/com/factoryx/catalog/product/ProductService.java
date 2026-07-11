package com.factoryx.catalog.product;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Sku sku, String name, Money price) {
        var product = Product.create(sku, name, price);
        return productRepository.save(product);
    }

    @Transactional
    public Product updatePrice(ProductId id, Money newPrice) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new DomainRuleViolation("Product not found: " + id.value()));
        product.updatePrice(newPrice);
        return productRepository.save(product);
    }

    @Transactional
    public Product applyDiscount(ProductId id, int percent) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new DomainRuleViolation("Product not found: " + id.value()));
        product.applyDiscount(percent);
        return productRepository.save(product);
    }

    @Transactional
    public Product rename(ProductId id, ProductName newName) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new DomainRuleViolation("Product not found: " + id.value()));
        product.rename(newName);
        return productRepository.save(product);
    }
}