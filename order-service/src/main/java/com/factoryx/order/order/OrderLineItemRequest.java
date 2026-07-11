package com.factoryx.order.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record OrderLineItemRequest(
        @NotNull UUID productId,
        @NotBlank String sku,
        @Positive int quantity) {
}
