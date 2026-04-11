package com.factoryx.inventory.stock

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/inventory")
class InventoryController(
    private val stockLevelRepository: StockLevelRepository,
    private val inventoryService: InventoryService
) {

    @GetMapping("/{sku}")
    fun getStock(@PathVariable sku: String): ResponseEntity<StockLevel> {
        val stock = stockLevelRepository.findById(sku)
        return stock.map { ResponseEntity.ok(it) }.orElseGet { ResponseEntity.notFound().build() }
    }

    @PostMapping("/{sku}/initialize")
    fun initStock(@PathVariable sku: String, @RequestParam quantity: Int): ResponseEntity<Void> {
        inventoryService.initializeStock(sku, quantity)
        return ResponseEntity.ok().build()
    }
}
