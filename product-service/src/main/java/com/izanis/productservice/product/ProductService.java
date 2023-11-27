package com.izanis.productservice.product;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
  Optional<Product> findById(@NotNull UUID id);

  @NotNull Product save(@NotNull Product product);

  List<Product> findAll();

  void update(@NotNull UUID id, @NotNull Product product);

  void deleteById(@NotNull UUID id);
}
