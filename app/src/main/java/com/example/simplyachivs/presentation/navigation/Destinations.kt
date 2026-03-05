package com.example.simplyachivs.presentation.navigation

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable object Splash
@Serializable object Login
@Serializable object Home
@Serializable object Target
@Serializable object Shop
@Serializable object Profile
@Serializable data class DetailTask(val taskId: String)
@Serializable data class GoalDetails(val goalId: String)
@Serializable object AddTask
@Serializable object AddTarget
@Serializable object AddAward
@Serializable object Settings
@Serializable object Achievements
@Serializable object Analytics
@Serializable object Asceticism
