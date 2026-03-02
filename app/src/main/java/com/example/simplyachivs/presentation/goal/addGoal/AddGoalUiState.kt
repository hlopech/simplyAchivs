package com.example.simplyachivs.presentation.goal.addGoal

import android.net.Uri
import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.goal.Step
import java.util.UUID

data class AddGoalUiState(
    val id: UUID? = null,
    val isLoading: Boolean = false,
    val goalName: String = "",
    val goalDescription: String = "",
    val newStepName: String = "",
    val steps: List<Step> = emptyList<Step>(),
    val error: AddGoalError? = null,
    val complexity: GoalComplexity? = null,
    val goalImage: Uri? = null,
    val goalNameError: String? = null,
    val newStepNameError: String? = null,
)
