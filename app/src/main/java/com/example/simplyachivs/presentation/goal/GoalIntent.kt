package com.example.simplyachivs.presentation.goal


sealed interface GoalIntent {
    object SelectActiveGoals: GoalIntent
    object SelectCompletedGoals: GoalIntent
    object AddNewGoal: GoalIntent
    object Refresh : GoalIntent
    data class OpenGoalDetails(val goalId: String): GoalIntent
}