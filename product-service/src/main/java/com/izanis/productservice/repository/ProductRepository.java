package com.izanis.productservice.repository;

import com.izanis.productservice.entity.Product;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  Optional<Product> findById(UUID id);

  @NonNull
  Product save(Product product);

  void deleteById(UUID id);

  @NonNull
  List<Product> findAll();

  List<Product> findByTotalInventoryLessThan(Integer quantity);

  long countByUpcIn(Collection<String> upc);

  Optional<Product> findByUpc(String upc);

  Page<Product> findAllByNameAndCategory(String name, Product.Category category, Pageable pageable);

  Page<Product> findAllByName(String name, Pageable pageable);

  Page<Product> findAllByCategory(Product.Category type, Pageable pageable);
}
