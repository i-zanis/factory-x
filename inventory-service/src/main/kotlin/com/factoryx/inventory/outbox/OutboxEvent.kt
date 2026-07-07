package com.factoryx.inventory.outbox

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "outbox")
class OutboxEvent(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val aggregateType: String,

    @Column(nullable = false)
    val aggregateId: String,

    @Column(name = "type", nullable = false)
    val type: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val payload: String,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
)
