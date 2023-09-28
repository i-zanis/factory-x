package com.izanis.productservice;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ProductPagedList extends PageImpl<ProductDto> {
  public ProductPagedList(List<ProductDto> content, Pageable pageable,
                          long total) {
    super(content, pageable, total);
  }

  public ProductPagedList(List<ProductDto> content) {
    super(content);
  }
}
