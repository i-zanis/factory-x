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

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

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

    public static Order create(UUID customerId) {
        return new Order(UUID.randomUUID(), customerId, OrderStatus.PENDING, new AuditInfo());
    }

    public void place(List<OrderLineItem> items, ProductPriceProvider priceProvider) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order can only be placed from PENDING status");
        }
        // TODO(i-zanis): need to replace this with something more idiomatic Apache/Spring etc
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("Order items cannot be empty");
        
        items.forEach(item -> item.validate(priceProvider));
        this.lineItems = items;

        this.totalPrice = items.stream()
                .map(OrderLineItem::subtotal)
                .reduce(Money.ZERO, Money::add);

        registerEvent(new OrderCreatedEvent(this));
    }

    public void complete() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only PENDING orders can be completed");
        }
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a COMPLETED order");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
