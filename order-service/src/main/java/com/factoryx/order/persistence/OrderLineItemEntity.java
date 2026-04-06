package com.factoryx.order.persistence;

import com.factoryx.common.domain.AuditInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "order_line_items")
@Getter
@Setter
@NoArgsConstructor
@SoftDelete
@EntityListeners(AuditingEntityListener.class)
public class OrderLineItemEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double unitPrice;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();

    public OrderLineItemEntity(UUID productId, String sku, int quantity, double unitPrice) {
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}
