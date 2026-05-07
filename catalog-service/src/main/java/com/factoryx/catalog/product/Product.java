package com.factoryx.catalog.product;

import com.factoryx.common.domain.AuditInfo;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Sku;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
@EntityListeners(AuditingEntityListener.class)
public class Product extends AbstractAggregateRoot<Product> {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "sku"))
    private Sku sku;

    private String name;

    private String description;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price"))
    private Money price;

    @Embedded
    private AuditInfo auditInfo;

    private Product(UUID id, Sku sku, String name, Money price) {
        if (id == null) throw new IllegalArgumentException("Product ID cannot be null");
        if (sku == null || sku.isEmpty()) throw new IllegalArgumentException("Product needs valid SKU");
        if (StringUtils.isBlank(name)) throw new IllegalArgumentException("Product name cannot be empty");
        if (price == null || price.isZero()) throw new IllegalArgumentException("Product price must be > 0");

        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.auditInfo = new AuditInfo();
    }

    public static Product create(Sku sku, String name, Money price) {
        return new Product(UUID.randomUUID(), sku, name, price);
    }

    public void describe(String description) {
        if (hasText(description)) {
            this.description = normalizeSpace(description);
            if (!isAsciiPrintable(this.description)) {
                throw new DomainRuleViolation("Description contains invalid characters");
            }
        } else {
            this.description = null;
        }
    }

    public void updatePrice(Money newPrice) {
        if (newPrice == null || newPrice.isZero()) throw new IllegalArgumentException("New price must be > 0");

        Money oldPrice = this.price;
        this.price = newPrice;

        registerEvent(new ProductPriceChangedEvent(this.id, oldPrice, newPrice));
    }

    public void applyDiscount(int percent) {
        if (percent <= 0 || percent > 50) {
            throw new DomainRuleViolation("Discount must be between 1% and 50%");
        }
        // TODO (Review): Is applying discount directly on entity safe without historical tracking?
        // TODO(i-zanis): Might need a PriceHistory entity in the future.
        updatePrice(this.price.discount(percent));
    }

    public void rename(String name) {
        if (!hasText(name)) {
            throw new DomainRuleViolation("Product name cannot be empty");
        }
        this.name = normalizeSpace(name);
        if (!isAsciiPrintable(this.name)) {
            throw new DomainRuleViolation("Product name contains invalid characters");
        }
    }
}
