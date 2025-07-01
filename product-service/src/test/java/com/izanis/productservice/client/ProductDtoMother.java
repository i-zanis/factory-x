package com.izanis.productservice.client;


import com.izanis.model.ProductDto;
import java.math.BigDecimal;

public class ProductDtoMother {
  static ProductDto.Builder basic() {
    return ProductDto.builder()
      .name("name")
      .price(BigDecimal.TEN);
  }
}