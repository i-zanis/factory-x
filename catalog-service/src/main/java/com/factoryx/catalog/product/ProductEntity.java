package com.factoryx.catalog.product;

import com.factoryx.common.domain.AuditInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SoftDelete
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {

    @Id
    private UUID id;
    private String sku;
    private String name;
    private double price;

    @Embedded
    private AuditInfo auditInfo = new AuditInfo();
}
