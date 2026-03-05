package com.example.simplyachivs.presentation.goal.goalDetails

import com.example.simplyachivs.domain.model.goal.Goal
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.model.goal.StepStatus

sealed class GoalDetailsUiState {
    object Loading : GoalDetailsUiState()
    data class Error(val message: String) : GoalDetailsUiState()
    data class Content(
        val goal: Goal,
        val steps: List<Step>,
        val isCompletingGoal: Boolean = false,
        val showCompleteDialog: Boolean = false,
        val showDeleteDialog: Boolean = false,
    ) : GoalDetailsUiState() {
        val completedCount: Int get() = steps.count { it.status == StepStatus.COMPLETED }
        val totalCount: Int get() = steps.size
        val progress: Float get() = if (totalCount == 0) 1f else completedCount.toFloat() / totalCount
        val allStepsDone: Boolean get() = totalCount == 0 || completedCount == totalCount
    }
}
