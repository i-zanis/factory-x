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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> lineItems = new ArrayList<>();

    @Embedded
    private Money totalPrice;

    @Embedded
    private AuditInfo auditInfo;

    private Order(OrderId id, CustomerId customerId, OrderStatus status, AuditInfo auditInfo) {
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
        this.lineItems.add(new OrderLineItem(productId, sku, quantity, price));
    }

    public OrderCreatedEvent place() {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainRuleViolation("Order already " + this.status);
        }

        Require.notEmpty(this.lineItems, "Cannot place empty order");
        this.totalPrice = this.lineItems.stream()
                .map(OrderLineItem::subtotal)
                .reduce(Money.ZERO, Money::add);

        OrderCreatedEvent event = new OrderCreatedEvent(
                this.id.value(),
                this.customerId.value(),
                this.totalPrice,
                this.lineItems.stream()
                        .map(item -> new OrderCreatedEvent.OrderLineItemInfo(
                                item.getProductId().value(),
                                item.getSku(),
                                item.getQuantity(),
                                item.getPrice()
                        ))
                        .toList()
        );
        registerEvent(event);
        return event;
    }

    public void approve() {
        if (this.status == OrderStatus.PENDING) {
            this.status = OrderStatus.APPROVED;
        } else {
            throw new DomainRuleViolation("Cannot approve order in status " + this.status);
        }
    }

    public void reject() {
        if (this.status == OrderStatus.PENDING) {
            this.status = OrderStatus.REJECTED;
        } else {
            throw new DomainRuleViolation("Cannot reject order in status " + this.status);
        }
    }

    public void fulfill() {
        if (this.status == OrderStatus.APPROVED) {
            this.status = OrderStatus.FULFILLED;
        } else {
            throw new DomainRuleViolation("Cannot fulfill order in status " + this.status);
        }
    }
}
