package com.izanis.productservice;

import java.util.UUID;

public interface ProductService {
  ProductDto findById(UUID id);
}
