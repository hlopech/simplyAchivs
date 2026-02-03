package com.example.simplyachivs.presentation.goal.addGoal

import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import com.example.simplyachivs.domain.model.goal.Step

data class AddGoalUiState(
    val isLoading: Boolean = false,
    val goalName: String = "",
    val goalDescription: String = "",
    val steps: List<Step> = emptyList(),
    val error: AddGoalError? = null,
    val complexity: GoalComplexity = GoalComplexity.EASY,
    val goalImage: Int? = null,

    )
