package com.example.simplyachivs.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.complexity.TaskComplexity
import com.example.simplyachivs.domain.model.task.Task
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.usecase.progress.GetAwadForTaskUseCase
import com.example.simplyachivs.domain.usecase.progress.GetUserProgressUseCase
import com.example.simplyachivs.domain.usecase.progress.UpdateStreakUseCase
import com.example.simplyachivs.domain.usecase.task.AddTaskUseCase
import com.example.simplyachivs.domain.usecase.task.CompleteTaskUseCase
import com.example.simplyachivs.domain.usecase.task.GetTasksUseCase
import com.example.simplyachivs.domain.usecase.task.ResetDailyTasksUseCase
import com.example.simplyachivs.domain.usecase.user.LoadUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val sessionRepository: SessionRepository,
    private val loadUserUseCase: LoadUserUseCase,
    private val loadUserProgressUseCase: GetUserProgressUseCase,
    private val updateStreakUseCase: UpdateStreakUseCase,
    private val getAwadForTaskUseCase: GetAwadForTaskUseCase,
    private val resetDailyTasksUseCase: ResetDailyTasksUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {

            val userId = sessionRepository.userId.first()
            if (userId == null) {
                sendEffect(HomeEffect.ShowError("Сессия не найдена. Перезапустите приложение"))
                return@launch
            }
            loadUserUseCase(userId).onSuccess { user ->
                _state.value = _state.value.copy(user = user)
            }
            resetDailyTasksUseCase(userId)
            loadTasksUseCase(userId).onSuccess { tasks ->
                _state.value = _state.value.copy(tasks = tasks)
            }
            loadUserProgressUseCase(userId).onSuccess { progress ->
                _state.value = _state.value.copy(userProgress = progress)
            }

        }
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

                val taskToAdd = _state.value.newTask!!.copy(
                    id = UUID.randomUUID(),
                    createdAt = Instant.now(),
                    userId = _state.value.user?.id
                )

                _state.update {
                    it.copy(
                        taskNameError = null,
                        tasks = it.tasks ?: emptyList(),
                        newTask = it.newTask?.copy(name = "")
                    )
                }

                sendEffect(HomeEffect.HideAddTaskSheet)

                viewModelScope.launch {
                    addTaskUseCase(taskToAdd).onSuccess {
                        _state.update { it.copy(tasks = it.tasks?.plus(taskToAdd)) }
                    }
                }
            }

            is HomeIntent.CompleteTask -> {
                val isCompleting = intent.task.completedAt == null
                viewModelScope.launch {
                    completeTaskUseCase(intent.task).onSuccess {
                        _state.update {
                            it.copy(tasks = it.tasks?.map { task ->
                                if (task.id == intent.task.id) task.copy(completedAt = if (isCompleting) Instant.now() else null)
                                else task
                            })
                        }

                        if (isCompleting) {
                            val currentProgress = _state.value.userProgress ?: return@onSuccess
                            val userId = _state.value.user?.id ?: return@onSuccess

                            updateStreakUseCase(userId, currentProgress)
                            loadUserProgressUseCase(userId).onSuccess { progressAfterStreak ->
                                getAwadForTaskUseCase(userId, intent.task.complexity, progressAfterStreak).onSuccess {
                                    loadUserProgressUseCase(userId).onSuccess { fresh ->
                                        _state.update { it.copy(userProgress = fresh) }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is HomeIntent.ChangeTaskName -> {
                val trimmed = intent.name.take(50)
                _state.update {
                    it.copy(
                        newTask = it.newTask?.copy(name = trimmed), taskNameError = null
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