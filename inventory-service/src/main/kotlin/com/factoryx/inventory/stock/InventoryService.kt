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
            val stockLevel = StockLevel(sku, Quantity(initialQuantity))
            val transactionLog = stockLevel.updateStock(initialQuantity)
            stockLevelRepository.save(stockLevel)
            stockTransactionLogRepository.save(transactionLog)
        }
    }

    @Transactional
    fun updateStock(skuValue: String, quantityChange: Int) {
        val sku = Sku(skuValue)
        val stockLevel =
            stockLevelRepository.findById(sku).orElseThrow { IllegalArgumentException("SKU not found: $skuValue") }
        val transactionLog = stockLevel.updateStock(quantityChange)
        stockLevelRepository.save(stockLevel)
        stockTransactionLogRepository.save(transactionLog)
    }
}
