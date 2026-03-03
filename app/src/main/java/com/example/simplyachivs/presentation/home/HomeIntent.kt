package com.example.simplyachivs.presentation.home

import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.domain.model.task.Task
import java.util.UUID

sealed interface HomeIntent {
    object ShowAddTaskSheet : HomeIntent
    object HideAddTaskSheet : HomeIntent
    object OpenProfile : HomeIntent

    data class ChangeTaskName(val name: String) : HomeIntent

    data class SelectTaskComplexity(val complexity: TaskComplexity) : HomeIntent

    object AddTask : HomeIntent
    data class CompleteTask(val task: Task) : HomeIntent
}