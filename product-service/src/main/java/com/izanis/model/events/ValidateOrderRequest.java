package com.izanis.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
// TODO(i-zanis): Does this need to be a Record?
public class ValidateOrderRequest {
  private ProductOrderDto productOrderDto;
}
