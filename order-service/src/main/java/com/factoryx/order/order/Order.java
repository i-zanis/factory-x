package com.factoryx.order.order;

import com.factoryx.common.domain.AuditInfo;
import com.factoryx.common.domain.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @AttributeOverride(name = "amount", column = @Column(name = "total_price"))
    private Money totalPrice;

    @Embedded
    private AuditInfo auditInfo;

    private Order(UUID id, UUID customerId, OrderStatus status, AuditInfo auditInfo) {
        if (id == null) throw new IllegalArgumentException("Order ID required");
        if (customerId == null) throw new IllegalArgumentException("Customer ID required");

        this.id = id;
        this.customerId = customerId;
        this.status = status != null ? status : OrderStatus.PENDING;
        this.auditInfo = auditInfo != null ? auditInfo : new AuditInfo();
    }

    public static Order create(CustomerId customerId) {
        return new Order(OrderId.generate(), customerId, OrderStatus.PENDING, new AuditInfo());
    }

    public List<OrderLineItem> getLineItems() {
        return Collections.unmodifiableList(lineItems);
    }

    public void addLineItem(OrderLineItem item) {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainRuleViolation("Cannot add items to " + this.status + " order");
        }
        this.lineItems.add(item);
    }

    public OrderCreatedEvent place() {
        if (this.status != OrderStatus.PENDING) {
            throw new DomainRuleViolation("Order already " + this.status);
        }
        // TODO(i-zanis): need to replace this with something more idiomatic Apache/Spring etc
        // TODO(i-zanis): DDD Violation - Domain entity should not depend on Spring Framework (CollectionUtils). Use pure Java.
        if (CollectionUtils.isEmpty(this.lineItems)) {
            throw new DomainRuleViolation("Cannot place empty order");
        }
        this.totalPrice = this.lineItems.stream()
                .map(OrderLineItem::subtotal)
                .reduce(Money.ZERO, Money::add);

        registerEvent(new OrderCreatedEvent(this));
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
