package com.factoryx.inventory.stock

import jakarta.persistence.Embeddable
import java.io.Serializable
import java.util.*

@Embeddable
data class StockTransactionLogId(val value: UUID = UUID.randomUUID()) : Serializable {
    companion object {
        fun generate(): StockTransactionLogId = StockTransactionLogId(UUID.randomUUID())
    }
}
