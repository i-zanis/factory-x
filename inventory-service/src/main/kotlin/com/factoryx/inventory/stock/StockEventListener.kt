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
                sku = event.sku.value(),
                quantityChange = change,
                reason = "REPLENISH"
            )
        )
    }

    @EventListener
    fun onStockConsumed(event: StockConsumedEvent) {
        val change = event.newQuantity.value() - event.oldQuantity.value()
        stockTransactionLogRepository.save(
            StockTransactionLog(
                sku = event.sku.value(),
                quantityChange = change,
                reason = "CONSUME"
            )
        )
    }
}
