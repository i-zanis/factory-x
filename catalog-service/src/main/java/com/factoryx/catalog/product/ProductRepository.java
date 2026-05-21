package com.factoryx.catalog.product;

import com.factoryx.common.domain.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductId> {
    Optional<Product> findBySku(Sku sku);

    List<Product> findAllBySkuIn(List<Sku> skus);

    @Query("SELECT p.id as id, p.sku as sku, p.name as name, p.price as price FROM Product p")
    List<ProductProjection> findAllProjectedBy();
}