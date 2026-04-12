package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryService(
    private val stockLevelRepository: StockLevelRepository
) {

    @Transactional
    fun initializeStock(skuValue: String, initialQuantity: Int) {
        val sku = Sku(skuValue)
        if (!stockLevelRepository.existsById(sku)) {
            val stockLevel = StockLevel(sku, Quantity(0))
            stockLevel.replenish(Quantity(initialQuantity))
            stockLevelRepository.save(stockLevel)
        }
    }

    @Transactional
    fun updateStock(skuValue: String, quantityChange: Int) {
        val sku = Sku(skuValue)
        val stockLevel =
            stockLevelRepository.findById(sku).orElseThrow { IllegalArgumentException("SKU not found: $skuValue") }

        if (quantityChange >= 0) {
            stockLevel.replenish(Quantity(quantityChange))
        } else {
            stockLevel.consume(Quantity(quantityChange))
        }
        
        stockLevelRepository.save(stockLevel)
    }
}

