package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/inventory")
@Validated
class InventoryController(
    private val stockLevelRepository: StockLevelRepository,
    private val inventoryService: InventoryService
) {

    data class InitializeStockRequest(
        @field:Positive(message = "Quantity must be strictly positive")
        val quantity: Int
    )

    @GetMapping("/{sku}")
    fun getStock(@PathVariable sku: String): ResponseEntity<StockLevel> {
        val stock = stockLevelRepository.findById(Sku(sku))
        return stock.map { ResponseEntity.ok(it) }.orElseGet { ResponseEntity.notFound().build() }
    }

    @PostMapping("/{sku}/initialize")
    fun initStock(
        @PathVariable sku: String,
        @Valid @RequestBody request: InitializeStockRequest
    ): ResponseEntity<Void> {
        inventoryService.initializeStock(Sku(sku), Quantity(request.quantity))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{sku}/replenish")
    fun replenish(
        @PathVariable sku: String,
        @RequestParam @Positive(message = "Quantity must be strictly positive") quantity: Int
    ): ResponseEntity<Void> {
        inventoryService.updateStock(Sku(sku), quantity)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{sku}/consume")
    fun consume(
        @PathVariable sku: String,
        @RequestParam @Positive(message = "Quantity must be strictly positive") quantity: Int
    ): ResponseEntity<Void> {
        inventoryService.updateStock(Sku(sku), -quantity)
        return ResponseEntity.ok().build()
    }
}
