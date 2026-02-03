package com.example.simplyachivs.presentation.goal.addGoal

import com.example.simplyachivs.domain.model.complexity.GoalComplexity

sealed interface AddGoalIntent {

    object AddNewGoal : AddGoalIntent
    object GoBack : AddGoalIntent

    data class ChangeGoalName(val name: String) : AddGoalIntent
    data class ChangeGoalDescription(val description: String) : AddGoalIntent
    data class AddNewStep(val step: String) : AddGoalIntent
    data class ChangeGoalComplexity(val complexity: GoalComplexity) : AddGoalIntent
    data class SelectGoalImage(val image: Int) : AddGoalIntent
    data class DeleteStep(val step: String) : AddGoalIntent

}