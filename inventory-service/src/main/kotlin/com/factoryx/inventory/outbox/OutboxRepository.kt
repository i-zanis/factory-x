package com.factoryx.inventory.outbox

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OutboxRepository : JpaRepository<OutboxEvent, UUID> {
    fun findTop100ByOrderByCreatedAtAsc(): List<OutboxEvent>
}
