package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Convert(converter = ProductIdConverter.class)
    private ProductId productId;

    @Embedded
    private Sku sku;

    @Embedded
    private Quantity quantity;

    @Embedded
    private Money price;

    public OrderLineItem(ProductId productId, Sku sku, Quantity quantity, Money price) {
        if (productId == null) throw new IllegalArgumentException("Product ID required");
        if (sku == null) throw new IllegalArgumentException("SKU required");
        if (quantity == null || quantity.isZero()) throw new IllegalArgumentException("Valid quantity required");
        if (price == null || price.isZero()) throw new IllegalArgumentException("Valid price required");

        this.productId = productId;
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
    }

    public Money subtotal() {
        return price.multiply(quantity.value());
    }
}
