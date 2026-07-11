package com.factoryx.inventory.stock

import com.factoryx.common.domain.DomainRuleViolation
import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import jakarta.persistence.*
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
@Table(name = "stock_levels")
class StockLevel private constructor(
    @Id
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "sku"))
    val sku: Sku,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "quantity"))
    private var quantity: Quantity,

    @Version
    private var version: Long? = null
) : AbstractAggregateRoot<StockLevel>() {

    init {
        if (quantity.value() < 0) throw DomainRuleViolation("Initial quantity cannot be negative")
    }

    fun currentQuantity(): Quantity = quantity

    fun replenish(quantityToAdd: Quantity) {
        if (quantityToAdd.isZero) throw DomainRuleViolation("Must replenish positive quantity")

        val oldQuantity = this.quantity
        this.quantity = this.quantity.add(quantityToAdd)

        registerEvent(StockReplenishedEvent(sku, oldQuantity, this.quantity))
    }

    fun consume(quantityToSubtract: Quantity) {
        if (quantityToSubtract.isZero) throw DomainRuleViolation("Must consume positive quantity")
        if (this.quantity.isLessThan(quantityToSubtract)) {
            throw DomainRuleViolation("Insufficient stock for SKU: ${sku.value()}")
        }

        val oldQuantity = this.quantity
        this.quantity = this.quantity.subtract(quantityToSubtract)

        registerEvent(StockConsumedEvent(sku, oldQuantity, this.quantity))
    }

    companion object {
        @JvmStatic
        fun create(sku: Sku, initialQuantity: Quantity): StockLevel {
            if (initialQuantity.value <= 0) {
                throw DomainRuleViolation("Initial quantity must be strictly positive")
            }
            return StockLevel(sku, initialQuantity)
        }
    }
}
