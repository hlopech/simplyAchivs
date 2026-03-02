package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "userProgress",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE

    )]
)
data class UserProgressEntity(
    @PrimaryKey
    val id: UUID,
    val userId: UUID,
    val xp: Int,
    val coin: Int,
    val streak: Int,
    val updatedAt: Instant
)
