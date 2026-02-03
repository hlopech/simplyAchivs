package com.example.simplyachivs.presentation.home

import com.example.simplyachivs.domain.model.task.Task
import java.time.Instant

data class HomeUiState(
    val isLoading: Boolean = false,
    val user: String,
    val error: String? = null,
    val tasks: List<Task> = emptyList(),
    val newTask: Task? = null,
    val currentTime: Instant,
    val streak: Int = 0,
    val xp: Int = 0,
    val coins: Int = 0,
    val isAddSheetVisible: Boolean = false
)
