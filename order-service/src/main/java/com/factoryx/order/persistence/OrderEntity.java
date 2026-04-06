package com.factoryx.order.persistence;

import com.factoryx.common.domain.AuditInfo;
import com.factoryx.order.domain.OrderStatus;
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

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();

    public OrderEntity(UUID id, UUID customerId, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
    }
}
