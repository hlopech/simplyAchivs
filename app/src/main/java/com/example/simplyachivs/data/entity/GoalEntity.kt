package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.goal.GoalStatus
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "goals",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]

)
data class GoalEntity(
    @PrimaryKey
    val id: UUID,
    val userId: UUID,
    val status: GoalStatus,
    val image: Int,
    val name: String,
    val description: String,
    val complexity: GoalComplexity,
    val createdAt: Instant,
    val completedAt: Instant? = null,


    )
