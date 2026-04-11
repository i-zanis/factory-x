package com.factoryx.inventory.stock

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "stock_levels")
class StockLevel(
    @Id
    var sku: String,
    var quantity: Int
) {
    fun updateStock(quantityChange: Int): StockTransactionLog {
        this.quantity += quantityChange
        val reason = if (quantityChange > 0) "Stock replenished" else "Stock consumed"
        return StockTransactionLog(sku = this.sku, quantityChange = quantityChange, reason = reason)
    }
}
