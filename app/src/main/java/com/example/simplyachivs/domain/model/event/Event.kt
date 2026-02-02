package com.example.simplyachivs.domain.model.event

import java.time.Instant
import java.util.UUID

data class Event(
    val id: UUID,
    val userId: UUID,
    val type: EventType,
    val relatedEntityId: UUID?,
    val createdAt: Instant
){
    companion object {
        fun now(
            userId: UUID,
            type: EventType,
            relatedEntityId: UUID? = null
        ): Event = Event(
            id = UUID.randomUUID(),
            userId = userId,
            type = type,
            relatedEntityId = relatedEntityId,
            createdAt = Instant.now()
        )
    }
}
