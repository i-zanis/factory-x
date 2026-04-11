package com.factoryx.inventory.stock

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryService(
    private val stockLevelRepository: StockLevelRepository,
    private val stockTransactionLogRepository: StockTransactionLogRepository
) {

    @Transactional
    fun initializeStock(sku: String, initialQuantity: Int) {
        if (!stockLevelRepository.existsById(sku)) {
            val stockLevel = StockLevel(sku, initialQuantity)
            val transactionLog = stockLevel.updateStock(initialQuantity)
            stockLevelRepository.save(stockLevel)
            stockTransactionLogRepository.save(transactionLog)
        }
    }

    @Transactional
    fun updateStock(sku: String, quantityChange: Int) {
        val stockLevel =
            stockLevelRepository.findById(sku).orElseThrow { IllegalArgumentException("SKU not found: $sku") }
        val transactionLog = stockLevel.updateStock(quantityChange)
        stockLevelRepository.save(stockLevel)
        stockTransactionLogRepository.save(transactionLog)
    }
}
