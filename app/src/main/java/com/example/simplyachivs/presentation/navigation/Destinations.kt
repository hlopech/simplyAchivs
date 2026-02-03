package com.example.simplyachivs.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable object Home
@Serializable object Target
@Serializable object Shop
@Serializable object Profile
@Serializable data class DetailTask(val taskId: String)
@Serializable object AddTask
@Serializable object AddTarget
@Serializable object AddAward
