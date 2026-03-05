package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simplyachivs.domain.model.asceticism.AsceticismCheckIn
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "asceticism_checkins",
    foreignKeys = [ForeignKey(
        entity = AsceticismEntity::class,
        parentColumns = ["id"],
        childColumns = ["asceticismId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("asceticismId")],
)
data class AsceticismCheckInEntity(
    @PrimaryKey val id: UUID,
    val asceticismId: UUID,
    val epochDay: Long,
    val completedAt: Instant,
)

fun AsceticismCheckIn.toEntity() = AsceticismCheckInEntity(
    id = id, asceticismId = asceticismId, epochDay = epochDay, completedAt = completedAt,
)

fun AsceticismCheckInEntity.toDomain() = AsceticismCheckIn(
    id = id, asceticismId = asceticismId, epochDay = epochDay, completedAt = completedAt,
)
