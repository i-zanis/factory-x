package com.factoryx.inventory.stock

import org.springframework.data.jpa.repository.JpaRepository

interface StockTransactionLogRepository : JpaRepository<StockTransactionLog, StockTransactionLogId>
