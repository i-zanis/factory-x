package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import jakarta.persistence.*
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
@Table(name = "stock_levels")
class StockLevel(
    @Id
    @AttributeOverride(name = "value", column = Column(name = "sku"))
    val sku: Sku,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "quantity"))
    private var quantity: Quantity
) : AbstractAggregateRoot<StockLevel>() {

    init {
        if (quantity.value() < 0) throw IllegalArgumentException("Initial quantity cannot be negative")
    }

    fun currentQuantity(): Quantity = quantity

    fun replenish(quantityToAdd: Quantity) {
        if (quantityToAdd.value() <= 0) throw IllegalArgumentException("Must replenish positive quantity")

        val oldQuantity = this.quantity
        this.quantity = Quantity(this.quantity.value() + quantityToAdd.value())

        registerEvent(StockReplenishedEvent(sku, oldQuantity, this.quantity))
    }

    fun consume(quantityToSubtract: Quantity) {
        val amountToSubtract = quantityToSubtract.value()
        if (amountToSubtract <= 0) throw IllegalArgumentException("Must consume positive quantity")
        if (this.quantity.value() < amountToSubtract) {
            throw IllegalStateException("Insufficient stock for SKU: ${sku.value()}")
        }

        val oldQuantity = this.quantity
        this.quantity = Quantity(this.quantity.value() - amountToSubtract)

        registerEvent(StockConsumedEvent(sku, oldQuantity, this.quantity))
    }

    companion object {
        /**
         * DDD Factory Method: Initializes stock level.
         */
        @JvmStatic
        fun create(sku: Sku, initialQuantity: Quantity): StockLevel {
            return StockLevel(sku, initialQuantity)
        }
    }
}
