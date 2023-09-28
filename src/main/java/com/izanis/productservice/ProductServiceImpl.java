package com.izanis.productservice;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

  @Override
  public ProductDto findById(UUID id) {
    return null;
  }
}
