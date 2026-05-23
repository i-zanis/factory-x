package com.factoryx.inventory.stock

import com.factoryx.common.domain.DomainRuleViolation
import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.abs

@Service
class InventoryService(
    private val stockLevelRepository: StockLevelRepository
) {

    @Transactional
    fun initializeStock(sku: Sku, initialQuantity: Quantity) {
        if (!stockLevelRepository.existsById(sku)) {
            val stockLevel = StockLevel.create(sku, initialQuantity)
            stockLevelRepository.save(stockLevel)
        }
    }

    @Transactional
    fun updateStock(sku: Sku, quantityChange: Int) {
        val stockLevel =
            stockLevelRepository.findById(sku).orElseThrow { DomainRuleViolation("SKU not found: ${sku.value()}") }

        if (quantityChange >= 0) {
            stockLevel.replenish(Quantity.of(quantityChange))
        } else {
            stockLevel.consume(Quantity.of(abs(quantityChange)))
        }
        
        stockLevelRepository.save(stockLevel)
    }

    @Transactional
    fun updateStocks(updates: List<Pair<Sku, Int>>) {
        val skus = updates.map { it.first }
        val stockLevels = stockLevelRepository.findAllById(skus).associateBy { it.sku }

        updates.forEach { (sku, quantityChange) ->
            val stockLevel = stockLevels[sku] ?: throw DomainRuleViolation("SKU not found: ${sku.value()}")

            if (quantityChange >= 0) {
                stockLevel.replenish(Quantity.of(quantityChange))
            } else {
                stockLevel.consume(Quantity.of(abs(quantityChange)))
            }
        }

        stockLevelRepository.saveAll(stockLevels.values)
    }
}
