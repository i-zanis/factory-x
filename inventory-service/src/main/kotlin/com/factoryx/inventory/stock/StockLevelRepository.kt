package com.factoryx.inventory.stock

import org.springframework.data.jpa.repository.JpaRepository

interface StockLevelRepository : JpaRepository<StockLevel, String>
