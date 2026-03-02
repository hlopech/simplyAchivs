package com.example.simplyachivs.presentation.goal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.usecase.goal.GetActiveGoalUseCase
import com.example.simplyachivs.domain.usecase.goal.GetCompletedGoalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val getActiveGoalUseCase: GetActiveGoalUseCase,
    private val getCompletedGoalsUseCase: GetCompletedGoalsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<GoalUiState>(GoalUiState.ShowActiveGoals(emptyList()))
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GoalEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: GoalIntent) {

        when (intent) {
            is GoalIntent.OpenGoalDetails -> {
                viewModelScope.launch { _effect.emit(GoalEffect.NavigateToGoalDetails(intent.goalId)) }
            }

            is GoalIntent.AddNewGoal -> {
                viewModelScope.launch {
                    _effect.emit(GoalEffect.NavigateToCreateNewGoal)
                }
            }

            is GoalIntent.SelectActiveGoals -> {
                viewModelScope.launch {
                    _uiState.update {
                        GoalUiState.Loading
                    }
                    delay(2000)
                    _uiState.update {
                        GoalUiState.ShowActiveGoals(emptyList())
                    }
                }
            }

            is GoalIntent.SelectCompletedGoals -> {
                _uiState.update { GoalUiState.ShowCompletedGoals(emptyList()) }
            }
        }

    }


}