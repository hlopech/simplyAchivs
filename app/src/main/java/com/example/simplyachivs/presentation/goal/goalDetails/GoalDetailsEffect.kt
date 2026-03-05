package com.example.simplyachivs.presentation.goal.goalDetails

sealed interface GoalDetailsEffect {
    object NavigateBack : GoalDetailsEffect
    data class ShowError(val message: String) : GoalDetailsEffect
    data class GoalCompleted(val xp: Int, val coins: Int) : GoalDetailsEffect
    object GoalDeleted : GoalDetailsEffect
}
