package com.example.simplyachivs.presentation.goal.addGoal

sealed interface AddGoalError {
    object EmptyName : AddGoalError
    object InvalidStep : AddGoalError
    data class General(val message: String) : AddGoalError
}