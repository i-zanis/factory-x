package com.factoryx.inventory.stock

import com.factoryx.common.domain.Quantity
import com.factoryx.common.domain.Sku
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class InventoryControllerTest : StringSpec({
    val stockLevelRepository = mockk<StockLevelRepository>()
    val inventoryService = mockk<InventoryService>(relaxed = true)

    val sut: MockMvc =
        MockMvcBuilders.standaloneSetup(InventoryController(stockLevelRepository, inventoryService)).build()

    "initStock with valid quantity returns 200 OK" {
        val requestPayload = """
            {
              "quantity": 50
            }
        """.trimIndent()

        val actual = sut.perform(
            post("/api/v1/inventory/SKU-1234/initialize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload)
        )

        actual.andExpect(status().isOk)
        verify { inventoryService.initializeStock(Sku("SKU-1234"), Quantity(50)) }
    }
})
