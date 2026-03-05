package com.example.simplyachivs.domain.model.event

import java.time.Instant
import java.util.UUID

data class Event(
    val id: UUID,
    val userId: UUID,
    val type: EventType,
    val relatedEntityId: UUID? = null,
    val relatedEntityType: String? = null, // "GOAL" | "TASK" | "STEP" | "AWARD"
    val metadata: String? = null,          // JSON: {"complexity":"HARD","xp":300}
    val createdAt: Instant,
) {
    companion object {
        fun now(
            userId: UUID,
            type: EventType,
            relatedEntityId: UUID? = null,
            relatedEntityType: String? = null,
            metadata: String? = null,
        ) = Event(
            id = UUID.randomUUID(),
            userId = userId,
            type = type,
            relatedEntityId = relatedEntityId,
            relatedEntityType = relatedEntityType,
            metadata = metadata,
            createdAt = Instant.now(),
        )
    }
}
