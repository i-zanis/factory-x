package com.izanis.productservice.product;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Validated
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  private static void validateOnUpdate(UUID id, Product product) {
    if (id == null || !id.equals(product.getId())) {
      throw new IllegalArgumentException(
          "Product ID cannot be null or different from the product ID");
    }
  }

  private static void validateId(UUID id) {
    if (id == null) throw new IllegalArgumentException("Product ID cannot be null");
  }

  @Override
  public Optional<Product> findById(UUID id) {
    validateId(id);
    return productRepository.findById(id);
  }

  @Override
  public Product save(Product product) {
    if (product == null) throw new IllegalArgumentException("Product cannot be null");
    return productRepository.save(product);
  }

  @Override
  public List<Product> findAll() {
    return (List<Product>) productRepository.findAll();
  }

  @Override
  public void update(UUID id, Product product) {
    validateOnUpdate(id, product);
    productRepository.save(product);
  }

  @Override
  public void deleteById(UUID id) {
    validateId(id);
    productRepository.deleteById(id);
  }
}
