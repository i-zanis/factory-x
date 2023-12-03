package com.izanis.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serial;
import java.util.List;
import java.util.stream.Stream;

public class ProductPagedList extends PageImpl<ProductDto> {

  @Serial private static final long serialVersionUID = -7192418042536340998L;

  public ProductPagedList(List<ProductDto> content, Pageable pageable, long total) {
    super(content, pageable, total);
  }

  public ProductPagedList(List<ProductDto> content) {
    super(content);
  }
}
