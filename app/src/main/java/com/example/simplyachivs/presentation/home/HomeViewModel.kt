package com.example.simplyachivs.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.usecase.task.AddTaskUseCase
import com.example.simplyachivs.domain.usecase.task.CompleteTaskUseCase
import com.example.simplyachivs.domain.usecase.task.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        _state.update { it.copy(newTask = Task(name = "", complexity = TaskComplexity.EASY)) }
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.HideAddTaskSheet -> sendEffect(HomeEffect.HideAddTaskSheet)
            HomeIntent.ShowAddTaskSheet -> {

                sendEffect(HomeEffect.ShowAddTaskSheet)
            }

            HomeIntent.OpenProfile -> sendEffect(HomeEffect.NavigateToProfile)
            HomeIntent.AddTask -> {
                val name = _state.value.newTask?.name.orEmpty().trim()
                val error = when {
                    name.isBlank() -> "Введите название задачи"
                    name.length < 3 -> "Название должно быть не менее 3 символов"
                    else -> null
                }
                if (error != null) {
                    _state.update { it.copy(taskNameError = error) }
                    return
                }

                _state.update {
                    it.copy(
                        taskNameError = null,
                        newTask = it.newTask?.copy(
                            id = UUID.randomUUID(),
                            createdAt = Instant.now(),
                        )
                    )
                }

                if (_state.value.tasks == null) {
                    _state.update { it.copy(tasks = emptyList()) }
                }
                _state.update { it.copy(tasks = it.tasks?.plus(it.newTask!!)) }
                sendEffect(HomeEffect.HideAddTaskSheet)
                _state.update {
                    it.copy(
                        newTask = it.newTask?.copy(name = ""),
                    )
                }
            }

            is HomeIntent.CompleteTask ->
                _state.update {
                    it.copy(tasks = it.tasks!!.map { task ->
                        if (task.id == intent.taskId) {
                            task.copy(completedAt = if (_state.value.tasks!!.find { it.id == intent.taskId }?.completedAt == null) Instant.now() else null)
                        } else task
                    }
                    )

                }

            is HomeIntent.ChangeTaskName -> {
                val trimmed = intent.name.take(50)
                _state.update {
                    it.copy(
                        newTask = it.newTask?.copy(name = trimmed),
                        taskNameError = null
                    )
                }
            }
            is HomeIntent.SelectTaskComplexity -> _state.update {
                it.copy(
                    newTask = it.newTask?.copy(
                        complexity = intent.complexity
                    )
                )
            }
        }
    }

}