package com.factoryx.catalog.product;

import com.factoryx.common.domain.AuditInfo;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
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

    public ProductEntity(UUID id, String sku, String name, double price) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
    }
}
