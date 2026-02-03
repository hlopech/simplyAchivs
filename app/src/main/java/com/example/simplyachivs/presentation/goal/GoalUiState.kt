package com.example.simplyachivs.presentation.goal

import com.example.simplyachivs.domain.model.goal.Goal
import java.util.Objects

sealed class GoalUiState {
    object Loading : GoalUiState()
    data class ShowActiveGoals(val goals: List<Goal>) : GoalUiState()
    data class ShowCompletedGoals(val goals: List<Goal>) : GoalUiState()
    object WithoutActiveGoals : GoalUiState()
    object WithoutCompletedGoals : GoalUiState()
}
