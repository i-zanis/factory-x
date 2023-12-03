package com.izanis.productservice.web.mapper;

import com.izanis.model.ProductDto;
import com.izanis.productservice.entity.Product;
import com.izanis.productservice.service.inventory.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ProductMapperDecorator implements ProductMapper {
  private ProductInventoryService productInventoryService;
  private ProductMapper mapper;

  @Autowired
  public void setProductInventoryService(ProductInventoryService productInventoryService) {
    this.productInventoryService = productInventoryService;
  }

  @Autowired
  public void setMapper(ProductMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public ProductDto toProductDtoWithInventory(Product product) {
    Integer availableInventory = productInventoryService.findAvailableInventory(product.getId());
    ProductDto dto = mapper.toProductDto(product);
    return dto.toBuilder().availableInventory(availableInventory).build();
  }
}
