package com.example.simplyachivs.presentation.home

import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.model.user.User
import com.example.simplyachivs.domain.model.user.UserProgress
import java.time.Instant

data class HomeUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val tasks: List<Task>? = null,
    val newTask: Task? = null,
    val currentTime: Instant? = null,
    val userProgress: UserProgress? = null,
)
