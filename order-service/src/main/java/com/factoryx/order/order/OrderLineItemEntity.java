package com.factoryx.order.order;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
import com.factoryx.common.domain.Sku;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_line_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderLineItemEntity {

    @Id
    private UUID id = UUID.randomUUID();

    private UUID productId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sku"))
    private Sku sku;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "quantity"))
    private Quantity quantity;

    // TODO(i-zanis): see if this should be @Embedded with @AO
    private double price;

    public OrderLineItemEntity(UUID productId, Sku sku, Quantity quantity) {
        this.productId = productId;
        this.sku = sku;
        this.quantity = quantity;
    }

    public void validate(ProductPriceProvider priceProvider) {
        ProductPriceProvider.PriceInfo info = priceProvider.getPriceInfo(this.sku);
        if (!info.exists()) {
            throw new IllegalArgumentException("SKU not found in catalog: " + this.sku.value());
        }
        this.price = info.price().doubleValue();
    }

    public Money subtotal() {
        return Money.of(price).multiply(quantity.value());
    }
}
