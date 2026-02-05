package com.example.simplyachivs.presentation.goal.addGoal

import android.net.Uri
import com.example.simplyachivs.domain.model.complexity.GoalComplexity
import java.util.UUID

sealed interface AddGoalIntent {

    object AddNewGoal : AddGoalIntent
    object GoBack : AddGoalIntent

    object OpenImagePicker : AddGoalIntent

    data class ChangeGoalName(val name: String) : AddGoalIntent
    data class ChangeGoalDescription(val description: String) : AddGoalIntent
    data class ChangeNewStepName(val name: String) : AddGoalIntent
    data class AddNewStep(val stepName: String) : AddGoalIntent
    data class SelectGoalComplexity(val complexity: GoalComplexity) : AddGoalIntent
    data class SelectGoalImage(val imageUri: Uri?) : AddGoalIntent
    data class DeleteStep(val stepId: UUID, val position: Int) : AddGoalIntent

}