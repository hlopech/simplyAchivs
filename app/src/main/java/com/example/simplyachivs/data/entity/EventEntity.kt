package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplyachivs.domain.model.event.EventType
import java.time.Instant
import java.util.UUID

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: UUID,
    val userId: UUID,
    val type: EventType,
    val relatedEntityId: UUID? = null,
    val relatedEntityType: String? = null,
    val metadata: String? = null,
    val createdAt: Instant,
)
