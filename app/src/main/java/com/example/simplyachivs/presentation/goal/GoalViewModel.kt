package com.example.simplyachivs.presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.usecase.goal.GetActiveGoalUseCase
import com.example.simplyachivs.domain.usecase.goal.GetCompletedGoalsUseCase
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
class GoalViewModel @Inject constructor(
    private val getActiveGoalUseCase: GetActiveGoalUseCase,
    private val getCompletedGoalsUseCase: GetCompletedGoalsUseCase,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<GoalUiState>(GoalUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private lateinit var userId: UUID
    private val _effect = MutableSharedFlow<GoalEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            userId = sessionRepository.userId.first()!!
            loadActiveGoals()
        }
    }

    private fun loadActiveGoals() {
        viewModelScope.launch {
            _uiState.update { GoalUiState.Loading }
            getActiveGoalUseCase(userId)
                .onSuccess { goals ->
                    _uiState.update {
                        if (goals.isEmpty()) GoalUiState.WithoutActiveGoals
                        else GoalUiState.ShowActiveGoals(goals)
                    }
                }
                .onFailure { e ->
                    _effect.emit(GoalEffect.ShowError(e.message ?: "Ошибка загрузки целей"))
                    _uiState.update { GoalUiState.WithoutActiveGoals }
                }
        }
    }

    private fun loadCompletedGoals() {
        viewModelScope.launch {
            _uiState.update { GoalUiState.Loading }
            getCompletedGoalsUseCase(userId)
                .onSuccess { goals ->
                    _uiState.update {
                        if (goals.isEmpty()) GoalUiState.WithoutCompletedGoals
                        else GoalUiState.ShowCompletedGoals(goals)
                    }
                }
                .onFailure { e ->
                    _effect.emit(GoalEffect.ShowError(e.message ?: "Ошибка загрузки целей"))
                    _uiState.update { GoalUiState.WithoutCompletedGoals }
                }
        }
    }

    private fun refreshCurrentTab() {
        viewModelScope.launch {
            val isCompletedTab = _uiState.value is GoalUiState.ShowCompletedGoals
                    || _uiState.value is GoalUiState.WithoutCompletedGoals
            if (isCompletedTab) {
                getCompletedGoalsUseCase(userId).onSuccess { goals ->
                    _uiState.update {
                        if (goals.isEmpty()) GoalUiState.WithoutCompletedGoals
                        else GoalUiState.ShowCompletedGoals(goals)
                    }
                }
            } else {
                getActiveGoalUseCase(userId).onSuccess { goals ->
                    _uiState.update {
                        if (goals.isEmpty()) GoalUiState.WithoutActiveGoals
                        else GoalUiState.ShowActiveGoals(goals)
                    }
                }
            }
        }
    }

    fun processIntent(intent: GoalIntent) {
        when (intent) {
            is GoalIntent.OpenGoalDetails -> {
                viewModelScope.launch { _effect.emit(GoalEffect.NavigateToGoalDetails(intent.goalId)) }
            }
            is GoalIntent.AddNewGoal -> {
                viewModelScope.launch { _effect.emit(GoalEffect.NavigateToCreateNewGoal) }
            }
            is GoalIntent.SelectActiveGoals -> loadActiveGoals()
            is GoalIntent.SelectCompletedGoals -> loadCompletedGoals()
            is GoalIntent.Refresh -> refreshCurrentTab()
        }
    }
}