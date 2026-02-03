package com.example.simplyachivs.presentation.goal.goalDetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GoalDetails(goalId: String, onBack: () -> Unit) {
    Text("goalId: $goalId")
}