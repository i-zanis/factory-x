package com.factoryx.inventory.stock

data class StockLevelDto(
    val sku: String,
    val quantity: Int
) {
    companion object {
        fun from(stockLevel: StockLevel): StockLevelDto =
            StockLevelDto(
                sku = stockLevel.sku.value(),
                quantity = stockLevel.currentQuantity().value()
            )
    }
}
