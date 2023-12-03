package com.izanis.productservice.web.mapper;

import com.izanis.model.ProductDto;
import com.izanis.productservice.entity.Product;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
@Mapper
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper {
  Product toProduct(ProductDto productDto);

  ProductDto toProductDtoWithInventory(Product product);

  ProductDto toProductDto(Product product);
}

