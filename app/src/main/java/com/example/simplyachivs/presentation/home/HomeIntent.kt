package com.example.simplyachivs.presentation.home

import com.example.simplyachivs.domain.model.task.Task

sealed interface HomeIntent {
    object ShowAddTaskSheet : HomeIntent
    object HideAddTaskSheet : HomeIntent
    data class AddTask(val task: Task) : HomeIntent
    data class MarkTask(val taskId: String) : HomeIntent
    }