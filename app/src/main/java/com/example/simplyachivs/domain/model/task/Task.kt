package com.example.simplyachivs.domain.model.task

import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import java.time.Instant
import java.util.UUID

data class Task(
    val id: UUID? = null,
    val userId: UUID? = null,
    val name: String = "",
    val createdAt: Instant? = null,
    val complexity: TaskComplexity = TaskComplexity.EASY,
    val completedAt: Instant? = null,

    )
