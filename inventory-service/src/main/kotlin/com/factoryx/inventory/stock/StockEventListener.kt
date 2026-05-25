package com.factoryx.inventory.stock

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class StockEventListener(
    private val stockTransactionLogRepository: StockTransactionLogRepository
) {
    @EventListener
    fun onStockReplenished(event: StockReplenishedEvent) {
        val change = event.newQuantity.value() - event.oldQuantity.value()
        stockTransactionLogRepository.save(
            StockTransactionLog(
                sku = event.sku,
                quantityChange = com.factoryx.common.domain.Quantity(change),
                reason = "REPLENISH"
            )
        )
    }

    @EventListener
    fun onStockConsumed(event: StockConsumedEvent) {
        val change = event.oldQuantity.value() - event.newQuantity.value()
        stockTransactionLogRepository.save(
            StockTransactionLog(
                sku = event.sku,
                quantityChange = com.factoryx.common.domain.Quantity(change),
                reason = "CONSUME"
            )
        )
    }
}
