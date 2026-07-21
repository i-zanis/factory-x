package com.factoryx.catalog.product;

import com.factoryx.common.domain.AuditInfo;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Require;
import com.factoryx.common.domain.Sku;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import org.jspecify.annotations.Nullable;


import static org.apache.commons.lang3.StringUtils.isAsciiPrintable;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
@EntityListeners(AuditingEntityListener.class)
public class Product extends AbstractAggregateRoot<Product> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ProductId id;

    @Embedded
    private Sku sku;

    @Embedded
    private ProductName name;

    @Nullable
    private String description;

    @Embedded
    private Money price;

    @Embedded
    private AuditInfo auditInfo;

    private Product(ProductId id, Sku sku, ProductName name, Money price) {
        this.id = Require.nonNull(id, "Product ID");
        this.sku = Require.nonNull(sku, "SKU");
        this.name = Require.nonNull(name, "Product name");

        this.price = Require.nonNull(price, "Price");
        Require.argument(!price.isZero(), "Price must be > 0");
        
        this.auditInfo = new AuditInfo();
        registerEvent(new ProductCreatedEvent(this.id, this.price));
    }

    public static Product create(Sku sku, String name, Money price) {
        return new Product(ProductId.generate(), sku, new ProductName(name), price);
    }

    public void describe(@Nullable String description) {
        if (description != null && !description.isBlank()) {
            this.description = normalizeSpace(description);
            Require.argument(isAsciiPrintable(this.description), "Description contains invalid characters");
        } else {
            this.description = null;
        }
    }

    public void updatePrice(Money newPrice) {
        Require.nonNull(newPrice, "New price");
        Require.argument(!newPrice.isZero(), "New price must be > 0");

        Money oldPrice = this.price;
        this.price = newPrice;
        registerEvent(new ProductPriceChangedEvent(this.id, oldPrice, newPrice));
    }

    public void applyDiscount(int percent) {
        Require.in(percent, 1, 50, "Discount must be between 1% and 50%");
        updatePrice(this.price.discount(percent));
    }

    public void rename(ProductName name) {
        this.name = Require.nonNull(name, "Product name");
    }

}

