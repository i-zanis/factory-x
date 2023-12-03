package com.izanis.model.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NewInventoryEvent extends ProductEvent {
  //  public NewInventoryEvent(ProductDto newProductDto) {
  //    super(newProductDto);
  //  }
}
