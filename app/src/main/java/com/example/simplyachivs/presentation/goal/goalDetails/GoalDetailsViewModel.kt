package com.example.simplyachivs.presentation.goal.goalDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.event.Event
import com.example.simplyachivs.domain.model.event.EventType
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.model.goal.StepStatus
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.service.AchievementNotifier
import com.example.simplyachivs.domain.usecase.event.TrackEventUseCase
import com.example.simplyachivs.domain.usecase.goal.CompleteGoalUseCase
import com.example.simplyachivs.domain.usecase.goal.CompleteStepUseCase
import com.example.simplyachivs.domain.usecase.goal.DeleteGoalUseCase
import com.example.simplyachivs.domain.usecase.goal.GetGoalUseCase
import com.example.simplyachivs.domain.usecase.goal.GetStepsUseCase
import com.example.simplyachivs.domain.usecase.progress.GetUserProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GoalDetailsViewModel @Inject constructor(
    private val getGoalUseCase: GetGoalUseCase,
    private val getStepsUseCase: GetStepsUseCase,
    private val completeStepUseCase: CompleteStepUseCase,
    private val completeGoalUseCase: CompleteGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase,
    private val sessionRepository: SessionRepository,
    private val trackEventUseCase: TrackEventUseCase,
    private val achievementNotifier: AchievementNotifier,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GoalDetailsUiState>(GoalDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GoalDetailsEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private lateinit var userId: UUID

    fun load(goalId: String) {
        viewModelScope.launch {
            userId = sessionRepository.userId.first()!!
            val id = runCatching { UUID.fromString(goalId) }.getOrNull() ?: run {
                _uiState.update { GoalDetailsUiState.Error("Неверный идентификатор цели") }
                return@launch
            }
            loadGoalWithSteps(id)
        }
    }

    private suspend fun loadGoalWithSteps(goalId: UUID) {
        _uiState.update { GoalDetailsUiState.Loading }
        val goalResult = getGoalUseCase(goalId)
        val stepsResult = getStepsUseCase(goalId)

        if (goalResult.isFailure) {
            _uiState.update { GoalDetailsUiState.Error(goalResult.exceptionOrNull()?.message ?: "Ошибка загрузки цели") }
            return
        }
        _uiState.update {
            GoalDetailsUiState.Content(
                goal = goalResult.getOrThrow(),
                steps = stepsResult.getOrDefault(emptyList())
            )
        }
    }

    fun processIntent(intent: GoalDetailsIntent) {
        when (intent) {
            GoalDetailsIntent.GoBack -> {
                viewModelScope.launch { _effect.emit(GoalDetailsEffect.NavigateBack) }
            }
            is GoalDetailsIntent.ToggleStep -> toggleStep(intent.step)
            GoalDetailsIntent.RequestCompleteGoal -> {
                _uiState.update { state ->
                    (state as? GoalDetailsUiState.Content)?.copy(showCompleteDialog = true) ?: state
                }
            }
            GoalDetailsIntent.ConfirmCompleteGoal -> confirmCompleteGoal()
            GoalDetailsIntent.DismissCompleteDialog -> {
                _uiState.update { state ->
                    (state as? GoalDetailsUiState.Content)?.copy(showCompleteDialog = false) ?: state
                }
            }
            GoalDetailsIntent.RequestDeleteGoal -> {
                _uiState.update { state ->
                    (state as? GoalDetailsUiState.Content)?.copy(showDeleteDialog = true) ?: state
                }
            }
            GoalDetailsIntent.ConfirmDeleteGoal -> confirmDeleteGoal()
            GoalDetailsIntent.DismissDeleteDialog -> {
                _uiState.update { state ->
                    (state as? GoalDetailsUiState.Content)?.copy(showDeleteDialog = false) ?: state
                }
            }
        }
    }

    private fun confirmDeleteGoal() {
        val content = _uiState.value as? GoalDetailsUiState.Content ?: return
        _uiState.update { content.copy(showDeleteDialog = false) }
        viewModelScope.launch {
            deleteGoalUseCase(content.goal)
                .onSuccess { _effect.emit(GoalDetailsEffect.GoalDeleted) }
                .onFailure { e -> _effect.emit(GoalDetailsEffect.ShowError(e.message ?: "Ошибка удаления цели")) }
        }
    }

    private fun toggleStep(step: Step) {
        val content = _uiState.value as? GoalDetailsUiState.Content ?: return
        // Optimistic update
        _uiState.update {
            content.copy(
                steps = content.steps.map {
                    if (it.id == step.id) it.copy(status = it.status.toggled()) else it
                }
            )
        }
        val isCompleting = step.status == StepStatus.ACTIVE
        viewModelScope.launch {
            completeStepUseCase(step).onSuccess {
                trackEventUseCase(Event.now(
                    userId = userId,
                    type = if (isCompleting) EventType.STEP_COMPLETED else EventType.STEP_UNCOMPLETED,
                    relatedEntityId = step.id,
                    relatedEntityType = "STEP",
                ))
                achievementNotifier.checkForNewAchievements(userId)
            }.onFailure {
                // Revert on failure
                _uiState.update { content.copy(steps = content.steps) }
                _effect.emit(GoalDetailsEffect.ShowError("Не удалось обновить шаг"))
            }
        }
    }

    private fun confirmCompleteGoal() {
        val content = _uiState.value as? GoalDetailsUiState.Content ?: return
        _uiState.update { content.copy(showCompleteDialog = false, isCompletingGoal = true) }
        viewModelScope.launch {
            getUserProgressUseCase(userId)
                .onSuccess { progress ->
                    completeGoalUseCase(content.goal, progress)
                        .onSuccess {
                            trackEventUseCase(Event.now(
                                userId = userId,
                                type = EventType.GOAL_COMPLETED,
                                relatedEntityId = content.goal.id,
                                relatedEntityType = "GOAL",
                            ))
                            achievementNotifier.checkForNewAchievements(userId)
                            _effect.emit(
                                GoalDetailsEffect.GoalCompleted(
                                    xp = content.goal.complexity.xp,
                                    coins = content.goal.complexity.coins
                                )
                            )
                            _effect.emit(GoalDetailsEffect.NavigateBack)
                        }
                        .onFailure { e ->
                            _uiState.update { content.copy(isCompletingGoal = false) }
                            _effect.emit(GoalDetailsEffect.ShowError(e.message ?: "Ошибка завершения цели"))
                        }
                }
                .onFailure { e ->
                    _uiState.update { content.copy(isCompletingGoal = false) }
                    _effect.emit(GoalDetailsEffect.ShowError(e.message ?: "Ошибка загрузки прогресса"))
                }
        }
    }
}

private fun com.example.simplyachivs.domain.model.goal.StepStatus.toggled() =
    if (this == com.example.simplyachivs.domain.model.goal.StepStatus.ACTIVE)
        com.example.simplyachivs.domain.model.goal.StepStatus.COMPLETED
    else
        com.example.simplyachivs.domain.model.goal.StepStatus.ACTIVE
