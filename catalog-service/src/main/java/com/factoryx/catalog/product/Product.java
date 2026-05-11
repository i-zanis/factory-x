package com.factoryx.catalog.product;

import com.factoryx.common.domain.AuditInfo;
import com.factoryx.common.domain.DomainRuleViolation;
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

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isAsciiPrintable;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

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
    private Sku sku;

    private String name;

    private String description;

    @Embedded
    private Money price;

    @Embedded
    private AuditInfo auditInfo;

    private Product(UUID id, Sku sku, String name, Money price) {
        this.id = Require.nonNull(id, "Product ID");
        this.sku = Require.nonNull(sku, "SKU");

        this.name = Require.text(name, "Product name");
        if (!isAsciiPrintable(this.name)) {
            throw new DomainRuleViolation("Product name contains invalid characters");
        }
        this.name = normalizeSpace(name);


        this.price = Require.nonNull(price, "Price");
        if (price.isZero()) {
            throw new DomainRuleViolation("Price must be > 0");
        }
        
        this.auditInfo = new AuditInfo();
    }

    public static Product create(Sku sku, String name, Money price) {
        return new Product(UUID.randomUUID(), sku, name, price);
    }

    public void describe(String description) {
        if (description != null && !description.isBlank()) {
            this.description = normalizeSpace(description);
            if (!isAsciiPrintable(this.description)) {
                throw new DomainRuleViolation("Description contains invalid characters");
            }
        } else {
            this.description = null;
        }
    }

    public void updatePrice(Money newPrice) {
        Require.nonNull(newPrice, "New price");
        // TODO(i-zanis): is this correct? can price be 0 or should be moved to the entity directly?
        if (newPrice.isZero()) {
            throw new DomainRuleViolation("New price must be > 0");
        }

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
        // Answer: Yes. Overwriting price destroys audit trail. Unsafe for financial reporting/refunds. Need PriceHistory entity or track price change domain events. Keep immutable record.
        updatePrice(this.price.discount(percent));
    }

    public void rename(String name) {
        this.name = Require.text(name, "Product name");
        if (!isAsciiPrintable(this.name)) {
            throw new DomainRuleViolation("Product name contains invalid characters");
        }
        this.name = normalizeSpace(name);
    }
}
