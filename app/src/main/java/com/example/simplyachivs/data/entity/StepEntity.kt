package com.example.simplyachivs.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.simplyachivs.domain.model.goal.StepStatus
import java.util.UUID

@Entity(
    tableName = "steps",
    foreignKeys = [ForeignKey(
        entity = GoalEntity::class,
        parentColumns = ["id"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("goalId")]
)
data class StepEntity(
    @PrimaryKey
    val id: UUID,
    val goalId: UUID,
    val name: String,
    val status: StepStatus,
    val position: Int
)
