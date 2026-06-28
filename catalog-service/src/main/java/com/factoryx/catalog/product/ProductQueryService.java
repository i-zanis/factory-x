package com.factoryx.catalog.product;

import com.factoryx.common.domain.Sku;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Done: CQRS separation — queries bypass the aggregate write model
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {

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

    public List<Product> getProductsBySkus(List<Sku> skus) {
        return productRepository.findAllBySkuIn(skus);
    }
}
