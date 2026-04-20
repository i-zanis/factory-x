package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
@Table(name = "stock_levels")
class StockLevel(
    @Id
    val sku: Sku,

    @Embedded
    private var quantity: Quantity
) : AbstractAggregateRoot<StockLevel>() {

    init {
        if (quantity.value() < 0) throw IllegalArgumentException("Initial quantity cannot be negative")
    }

    fun currentQuantity(): Quantity = quantity

    fun replenish(quantityToAdd: Quantity) {
        if (quantityToAdd.isZero) throw IllegalArgumentException("Must replenish positive quantity")

        val oldQuantity = this.quantity
        this.quantity = this.quantity.add(quantityToAdd)

        registerEvent(StockReplenishedEvent(sku, oldQuantity, this.quantity))
    }

    fun consume(quantityToSubtract: Quantity) {
        if (quantityToSubtract.isZero) throw IllegalArgumentException("Must consume positive quantity")
        if (this.quantity.isLessThan(quantityToSubtract)) {
            throw IllegalStateException("Insufficient stock for SKU: ${sku.value()}")
        }

        val oldQuantity = this.quantity
        this.quantity = this.quantity.subtract(quantityToSubtract)

        registerEvent(StockConsumedEvent(sku, oldQuantity, this.quantity))
    }

    companion object {
        @JvmStatic
        fun create(sku: Sku, initialQuantity: Quantity): StockLevel {
            return StockLevel(sku, initialQuantity)
        }
    }
}
