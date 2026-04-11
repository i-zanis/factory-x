package com.factoryx.inventory.stock

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StockTransactionLogRepository : JpaRepository<StockTransactionLog, UUID>
