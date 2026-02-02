package com.example.simplyachivs.domain.model.task

import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import java.time.Instant
import java.util.UUID

data class Task(
    val id: UUID,
    val userId:UUID,
    val name:String,
    val createdAt:Instant,
    val complexity: TaskComplexity,
    val completedAt: Instant?=null,

    )
