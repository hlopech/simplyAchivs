package com.example.simplyachivs.presentation.goal.addGoal

sealed interface AddGoalEffect {
    data class ShowError(val message: String) : AddGoalEffect
    object NavigateToGoals : AddGoalEffect

//    object OpenHelpDialog : AddGoalEffect
    object LaunchImagePicker : AddGoalEffect
    object SelectComplexity : AddGoalEffect

}