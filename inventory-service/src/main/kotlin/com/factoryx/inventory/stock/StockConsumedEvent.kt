package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku

data class StockConsumedEvent(
    val sku: Sku,
    val oldQuantity: Quantity,
    val newQuantity: Quantity
)
