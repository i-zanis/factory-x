package com.izanis.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
// TODO(i-zanis): record?
public class ValidateOrderResult {
  private UUID orderId;
  private Boolean valid;
}