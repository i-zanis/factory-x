package com.factoryx.inventory.stock

import com.factoryx.common.domain.Sku
import org.springframework.data.jpa.repository.JpaRepository

interface StockLevelRepository : JpaRepository<StockLevel, Sku>
