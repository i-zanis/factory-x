package com.factoryx.order.order;

import com.factoryx.common.domain.AuditInfo;
import com.factoryx.common.domain.DomainRuleViolation;
import com.factoryx.common.domain.Money;
import com.factoryx.common.domain.Quantity;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
@EntityListeners(AuditingEntityListener.class)
public class Order extends AbstractAggregateRoot<Order> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private OrderId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "customer_id", nullable = false))
    private CustomerId customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private final List<OrderLineItem> lineItems = new ArrayList<>();

    @Embedded
    @Nullable
    private Money totalPrice;

    @Embedded
    private AuditInfo auditInfo;

    private Order(OrderId id, CustomerId customerId, @Nullable OrderStatus status, @Nullable AuditInfo auditInfo) {
        this.id = Require.nonNull(id, "Order ID");
        this.customerId = Require.nonNull(customerId, "Customer ID");
        this.status = status != null ? status : OrderStatus.PENDING;
        this.auditInfo = auditInfo != null ? auditInfo : new AuditInfo();
    }

    public static Order create(CustomerId customerId) {
        return new Order(OrderId.generate(), customerId, OrderStatus.PENDING, new AuditInfo());
    }

    public List<OrderLineItem> getLineItems() {
        return Collections.unmodifiableList(lineItems);
    }

    public void addLineItem(ProductId productId, Sku sku, Quantity quantity, Money price) {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainRuleViolation("Cannot add items to " + this.status + " order");
        }
        for (OrderLineItem existing : this.lineItems) {
            if (existing.getSku().equals(sku)) {
                existing.increaseQuantity(quantity);
                return;
            }
        }
        this.lineItems.add(OrderLineItem.create(productId, sku, quantity, price));
    }

    public void place() {
        transitionTo(OrderStatus.PLACED);

        Require.notEmpty(this.lineItems, "Cannot place empty order");

        this.totalPrice = this.lineItems.stream()
                .map(OrderLineItem::subtotal)
                .reduce(Money::add)
                .orElseThrow();

        registerEvent(new OrderCreatedEvent(
                this.id,
                this.customerId,
                this.totalPrice,
                this.lineItems.stream()
                        .map(item -> new OrderCreatedEvent.OrderLineItemInfo(
                                item.getProductId(),
                                item.getSku(),
                                item.getQuantity(),
                                item.getPrice()
                        ))
                        .toList()
        ));
    }

    public void approve() {
        transitionTo(OrderStatus.APPROVED);
    }

    public void reject() {
        transitionTo(OrderStatus.REJECTED);
    }

    public void fulfill() {
        transitionTo(OrderStatus.FULFILLED);
    }

    private void transitionTo(OrderStatus target) {
        if (!this.status.canTransitionTo(target)) {
            throw new DomainRuleViolation("Cannot transition from " + this.status + " to " + target);
        }
        this.status = target;
    }
}
