package com.factoryx.order.order;

import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.Objects.*;

@Entity
@Table(name = "order_line_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
        this.productId = requireNonNull(productId, "Product ID is required");
        this.sku = requireNonNull(sku, "SKU is required");
        
        requireNonNull(quantity, "Quantity is required");
        if (quantity.isZero()) throw new DomainRuleViolation("Quantity must be greater than zero");
        this.quantity = quantity;

        requireNonNull(price, "Price is required");
        if (price.isZero()) throw new DomainRuleViolation("Price must be greater than zero");
        this.price = price;
    }

    public Money subtotal() {
        return price.multiply(quantity.value());
    }
}
