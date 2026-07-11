package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "stock_transaction_logs")
class StockTransactionLog private constructor(
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: StockTransactionLogId,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "sku"))
    val sku: Sku,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "quantity_change"))
    val quantityChange: Quantity,

    @Enumerated(EnumType.STRING)
    val reason: TransactionReason,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
) {
    companion object {
        fun create(sku: Sku, quantityChange: Quantity, reason: TransactionReason): StockTransactionLog =
            StockTransactionLog(
                id = StockTransactionLogId.generate(),
                sku = sku,
                quantityChange = quantityChange,
                reason = reason
            )
    }
}