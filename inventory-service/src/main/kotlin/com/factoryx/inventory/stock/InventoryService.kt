package com.factoryx.inventory.stock

import com.factoryx.common.domain.DomainRuleViolation
import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import com.factoryx.inventory.outbox.OutboxEvent
import com.factoryx.inventory.outbox.OutboxRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.math.abs

@Service
class InventoryService(
    private val stockLevelRepository: StockLevelRepository,
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper
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
            stockLevel.replenish(Quantity(quantityChange))
        } else {
            stockLevel.consume(Quantity(abs(quantityChange)))
        }
        
        stockLevelRepository.save(stockLevel)
    }

    @Transactional
    fun updateStocks(updates: List<Pair<Sku, Int>>) {
        val skus = updates.map { it.first }
        val stockLevels = stockLevelRepository.findAllById(skus).associateBy { it.sku }

        updates.forEach { (sku, quantityChange) ->
            val stockLevel = stockLevels[sku] ?: throw IllegalArgumentException("SKU not found: ${sku.value()}")

            if (quantityChange >= 0) {
                stockLevel.replenish(Quantity(quantityChange))
            } else {
                stockLevel.consume(Quantity(abs(quantityChange)))
            }
        }

        stockLevelRepository.saveAll(stockLevels.values)
    }

    @Transactional(noRollbackFor = [DomainRuleViolation::class])
    fun processOrderStockUpdate(orderId: String, updates: List<Pair<Sku, Int>>) {
        var status = "SUCCESS"
        try {
            updateStocks(updates)
        } catch (e: DomainRuleViolation) {
            status = "FAILED"
        }

        val responsePayload = mapOf("orderId" to orderId, "status" to status)
        outboxRepository.save(
            OutboxEvent(
                aggregateType = "Order",
                aggregateId = orderId,
                type = "InventoryResponse",
                payload = objectMapper.writeValueAsString(responsePayload)
            )
        )
    }
}
