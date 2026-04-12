package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import jakarta.persistence.*

@Entity
@Table(name = "stock_levels")
class StockLevel(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "sku"))
    var sku: Sku,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "quantity"))
    var quantity: Quantity
) {
    fun updateStock(quantityChange: Int): StockTransactionLog {
        val newQuantity = Quantity(this.quantity.value() + quantityChange)
        this.quantity = newQuantity
        val reason = if (quantityChange > 0) "Stock replenished" else "Stock consumed"
        return StockTransactionLog(sku = this.sku.value(), quantityChange = quantityChange, reason = reason)
    }
}
