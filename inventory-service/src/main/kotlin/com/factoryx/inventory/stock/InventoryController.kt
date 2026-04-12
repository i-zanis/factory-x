package com.factoryx.inventory.stock

import com.factoryx.common.domain.Sku
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/inventory")
class InventoryController(
    private val stockLevelRepository: StockLevelRepository,
    private val inventoryService: InventoryService
) {

    @GetMapping("/{sku}")
    fun getStock(@PathVariable sku: String): ResponseEntity<StockLevel> {
        val stock = stockLevelRepository.findById(Sku(sku))
        return stock.map { ResponseEntity.ok(it) }.orElseGet { ResponseEntity.notFound().build() }
    }

    @PostMapping("/{sku}/initialize")
    fun initStock(@PathVariable sku: String, @RequestParam quantity: Int): ResponseEntity<Void> {
        inventoryService.initializeStock(sku, quantity)
        return ResponseEntity.ok().build()
    }
}
