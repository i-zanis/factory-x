package com.izanis.productservice.client;

import com.izanis.productservice.product.dto.ProductDto;

import java.math.BigDecimal;

public class ProductDtoMother {
  static ProductDto.ProductDtoBuilder defaultProductDto() {
    return ProductDto.builder()
      .name("name")
      .price(BigDecimal.TEN);
  }
}