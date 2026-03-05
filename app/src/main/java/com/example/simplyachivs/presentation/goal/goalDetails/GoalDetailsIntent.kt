package com.example.simplyachivs.presentation.goal.goalDetails

import com.example.simplyachivs.domain.model.goal.Step

sealed interface GoalDetailsIntent {
    object GoBack : GoalDetailsIntent
    data class ToggleStep(val step: Step) : GoalDetailsIntent
    object RequestCompleteGoal : GoalDetailsIntent
    object ConfirmCompleteGoal : GoalDetailsIntent
    object DismissCompleteDialog : GoalDetailsIntent
    object RequestDeleteGoal : GoalDetailsIntent
    object ConfirmDeleteGoal : GoalDetailsIntent
    object DismissDeleteDialog : GoalDetailsIntent
}
