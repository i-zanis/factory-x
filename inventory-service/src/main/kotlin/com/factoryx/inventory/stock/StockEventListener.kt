package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
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
            StockTransactionLog.create(
                sku = event.sku,
                quantityChange = Quantity(change),
                reason = TransactionReason.REPLENISH
            )
        )
    }

    @EventListener
    fun onStockConsumed(event: StockConsumedEvent) {
        val change = event.oldQuantity.value() - event.newQuantity.value()
        stockTransactionLogRepository.save(
            StockTransactionLog.create(
                sku = event.sku,
                quantityChange = Quantity(change),
                reason = TransactionReason.CONSUME
            )
        )
    }
}
