package com.example.simplyachivs.presentation.goal

sealed interface GoalEffect {
    data class ShowError(val message: String) : GoalEffect
    object NavigateToCreateNewGoal : GoalEffect
    data class NavigateToGoalDetails(val goalId: String) : GoalEffect
}