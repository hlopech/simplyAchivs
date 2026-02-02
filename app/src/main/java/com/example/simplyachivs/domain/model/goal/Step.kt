package com.example.simplyachivs.domain.model.goal

import java.util.UUID

data class Step(
    val id: UUID,
    val goalId: UUID,
    val name: String,
    val status: StepStatus,
    val position:Int
)
