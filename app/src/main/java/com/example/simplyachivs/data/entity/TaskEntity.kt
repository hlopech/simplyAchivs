package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskEntity(
    @PrimaryKey
    val id: UUID,
    val userId: UUID? = null,
    val name: String = "",
    val createdAt: Instant? = null,
    val complexity: TaskComplexity = TaskComplexity.EASY,
    val completedAt: Instant? = null,
)
