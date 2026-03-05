package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simplyachivs.domain.model.asceticism.Asceticism
import com.example.simplyachivs.domain.model.asceticism.AsceticismStatus
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "ascetisms",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("userId")],
)
data class AsceticismEntity(
    @PrimaryKey val id: UUID,
    val userId: UUID,
    val title: String,
    val description: String,
    val emoji: String,
    val durationDays: Int,
    val startEpochDay: Long,
    val status: String,
    val createdAt: Instant,
)

fun Asceticism.toEntity() = AsceticismEntity(
    id = id, userId = userId, title = title, description = description,
    emoji = emoji, durationDays = durationDays, startEpochDay = startEpochDay,
    status = status.name, createdAt = createdAt,
)

fun AsceticismEntity.toDomain() = Asceticism(
    id = id, userId = userId, title = title, description = description,
    emoji = emoji, durationDays = durationDays, startEpochDay = startEpochDay,
    status = AsceticismStatus.valueOf(status), createdAt = createdAt,
)
