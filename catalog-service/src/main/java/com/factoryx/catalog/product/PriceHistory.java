sp package com.factoryx.catalog.product;

import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Require;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product_price_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "product_id", nullable = false))
    private ProductId productId;

    @Embedded
    private Money price;

    private Instant effectiveFrom;

    private PriceHistory(ProductId productId, Money price, Instant effectiveFrom) {
        this.productId = Require.nonNull(productId, "Product ID");
        this.price = Require.nonNull(price, "Price");
        this.effectiveFrom = Require.nonNull(effectiveFrom, "Effective from");
    }

    public static PriceHistory record(ProductId productId, Money price) {
        return new PriceHistory(productId, price, Instant.now());
    }
}
