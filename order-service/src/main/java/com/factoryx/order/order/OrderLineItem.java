package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Require;
import com.factoryx.common.domain.Sku;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "order_line_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineItem {

    @EmbeddedId
    private OrderLineItemId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "product_id", nullable = false))
    private ProductId productId;

    @Embedded
    private Sku sku;

    @Embedded
    private Quantity quantity;

    @Embedded
    private Money price;

    public OrderLineItem(ProductId productId, Sku sku, Quantity quantity, Money price) {
        this.id = new OrderLineItemId(UUID.randomUUID());
        this.productId = Require.nonNull(productId, "Product ID");
        this.sku = Require.nonNull(sku, "SKU");

        this.quantity = Require.nonNull(quantity, "Quantity");
        if (quantity.isZero()) {
            throw new DomainRuleViolation("Quantity must be greater than zero");
        }

        this.price = Require.nonNull(price, "Price");
        if (price.isZero()) {
            throw new DomainRuleViolation("Price must be greater than zero");
        }
    }

    public Money subtotal() {
        return price.multiply(quantity.value());
    }
}
