package com.factoryx.order.order;

import com.factoryx.catalog.grpc.InternalCatalogServiceGrpc;
import com.factoryx.catalog.grpc.PriceRequest;
import com.factoryx.catalog.grpc.PriceResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_line_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderLineItemEntity {

    @Id
    private UUID id = UUID.randomUUID();

    private UUID productId;
    
    private String sku;

    private int quantity;

    private double price; // Using double as per existing field, though BigDecimal is preferred for production

    public OrderLineItemEntity(UUID productId, String sku, int quantity) {
        this.productId = productId;
        this.sku = sku;
        this.quantity = quantity;
    }

    public void validate(InternalCatalogServiceGrpc.InternalCatalogServiceBlockingStub catalogStub) {
        if (this.quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        PriceResponse catalogResponse = catalogStub.getProductPrice(
                PriceRequest.newBuilder()
                        .setSku(this.sku)
                        .build()
        );

        if (!catalogResponse.getExists()) {
            throw new IllegalArgumentException("SKU not found in catalog: " + this.sku);
        }

        this.price = catalogResponse.getPrice();
    }
}
