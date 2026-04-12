package com.factoryx.order.order;

import com.factoryx.common.domain.AuditInfo;
import com.factoryx.common.domain.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@SoftDelete
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderLineItemEntity> lineItems = new ArrayList<>();

    @Column(nullable = false)
    private double totalPrice;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();

    public OrderEntity(UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.status = OrderStatus.PENDING;
    }

    public void place(List<OrderLineItemEntity> items, ProductPriceProvider priceProvider) {
        items.forEach(item -> item.validate(priceProvider));
        this.lineItems = items;

        // Aggregate Responsibility: Calculate total
        this.totalPrice = items.stream()
                .map(OrderLineItemEntity::subtotal)
                .reduce(Money.ZERO, Money::add)
                .doubleValue();
    }
}
