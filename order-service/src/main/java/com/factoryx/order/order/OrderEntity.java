package com.factoryx.order.order;

import com.factoryx.catalog.grpc.InternalCatalogServiceGrpc;
import com.factoryx.common.domain.AuditInfo;
import com.factoryx.order.outbox.OutboxEventEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public OrderEntity(UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.status = OrderStatus.PENDING;
    }

    public void place(List<OrderLineItemEntity> items, InternalCatalogServiceGrpc.InternalCatalogServiceBlockingStub catalogStub) {
        items.forEach(item -> item.validate(catalogStub));
        this.lineItems = items;
    }

    // TODO(i-zanis): to think if this should move out of here
    public OutboxEventEntity toOutboxEvent(ObjectMapper objectMapper) {
        try {
            return new OutboxEventEntity(
                    "Order",
                    this.id.toString(),
                    "OrderCreated",
                    objectMapper.writeValueAsString(this)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order for outbox event", e);
        }
    }
}
