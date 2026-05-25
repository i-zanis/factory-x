package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "stock_transaction_logs")
class StockTransactionLog(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "sku"))
    val sku: Sku,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "quantity_change"))
    val quantityChange: Quantity,

    val reason: String
)