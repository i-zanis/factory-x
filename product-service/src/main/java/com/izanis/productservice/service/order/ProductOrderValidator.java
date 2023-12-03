package com.izanis.productservice.service.order;

import static java.util.stream.Collectors.*;

import com.izanis.model.events.ProductOrderDto;
import com.izanis.model.events.ProductOrderLineDto;
import com.izanis.productservice.service.ProductService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductOrderValidator {
  private final ProductService productService;

  public boolean validateOrder(ProductOrderDto productOrderDto) {
    Set<String> upcs =
        productOrderDto.productOrderLines().stream().map(ProductOrderLineDto::upc).collect(toSet());
    if (upcs.isEmpty()) return false;
    long productsFound = productService.countByUpcIn(upcs);
    return upcs.size() - productsFound == 0;
  }
}
