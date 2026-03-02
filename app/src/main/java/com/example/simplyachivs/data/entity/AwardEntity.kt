package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simplyachivs.domain.model.award.AwardStatus
import java.time.Instant
import java.util.UUID
@Entity(tableName = "awards",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
    )
data class AwardEntity (
    @PrimaryKey
    val id: UUID,
    val userId: UUID,
    val status: AwardStatus,
    val image: Int,
    val name: String,
    val description: String,
    val price: Int,
    val createdAt: Instant? = null,
    val completedAt: Instant? = null
)