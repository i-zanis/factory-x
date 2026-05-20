package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "stock_transaction_logs")
class StockTransactionLog private constructor(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "sku"))
    val sku: Sku,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "quantity_change"))
    val quantityChange: Quantity,

    val reason: String
) {
    companion object {
        @JvmStatic
        fun log(sku: Sku, change: Quantity, reason: String): StockTransactionLog {
            return StockTransactionLog(sku = sku, quantityChange = change, reason = reason)
        }
    }
}
