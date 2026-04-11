package com.factoryx.inventory.stock

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "stock_transaction_logs")
class StockTransactionLog(
    @Id
    var id: UUID = UUID.randomUUID(),
    var sku: String,
    var quantityChange: Int,
    var reason: String
)
