package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku

object StockLevelMother {
    fun valid(quantity: Int = 10, skuValue: String = "SKU-1234"): StockLevel {
        return StockLevel.create(Sku(skuValue), Quantity(quantity))
    }

    fun outOfStock(skuValue: String = "SKU-OUT"): StockLevel {
        return StockLevel.create(Sku(skuValue), Quantity(0))
    }

    fun lowStock(skuValue: String = "SKU-LOW"): StockLevel {
        return StockLevel.create(Sku(skuValue), Quantity(2))
    }

    fun wellStocked(skuValue: String = "SKU-WELL"): StockLevel {
        return StockLevel.create(Sku(skuValue), Quantity(100))
    }
}
