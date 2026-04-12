package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryService(
    private val stockLevelRepository: StockLevelRepository,
    private val stockTransactionLogRepository: StockTransactionLogRepository
) {

    @Transactional
    fun initializeStock(skuValue: String, initialQuantity: Int) {
        val sku = Sku(skuValue)
        if (!stockLevelRepository.existsById(sku)) {
            val stockLevel = StockLevel(sku, Quantity(0))
            val transactionLog = stockLevel.replenish(Quantity(initialQuantity))
            stockLevelRepository.save(stockLevel)
            stockTransactionLogRepository.save(transactionLog)
        }
    }

    @Transactional
    fun updateStock(skuValue: String, quantityChange: Int) {
        val sku = Sku(skuValue)
        val stockLevel =
            stockLevelRepository.findById(sku).orElseThrow { IllegalArgumentException("SKU not found: $skuValue") }

        val transactionLog = if (quantityChange >= 0) {
            stockLevel.replenish(Quantity(quantityChange))
        } else {
            stockLevel.consume(Quantity(-quantityChange))
        }
        
        stockLevelRepository.save(stockLevel)
        // TODO(i-zanis): does this need to be moved to another layer?
        stockTransactionLogRepository.save(transactionLog)
    }
}
