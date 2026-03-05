package com.example.simplyachivs.domain.model.goal

import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import java.time.Instant
import java.util.UUID

data class Goal(
    val id: UUID,
    val userId: UUID,
    val status:GoalStatus,
    val image: String?,
    val name:String,
    val description:String,
    val complexity: GoalComplexity,
    val createdAt:Instant,
    val completedAt: Instant?=null,


    )
