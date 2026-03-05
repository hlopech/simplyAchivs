package com.example.simplyachivs.presentation.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.achievement.Achievement
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.usecase.achievement.GetAchievementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AchievementsUiState {
    object Loading : AchievementsUiState
    data class Error(val message: String) : AchievementsUiState
    data class Content(val achievements: List<Achievement>) : AchievementsUiState
}

@HiltViewModel
class AchievementsViewModel @Inject constructor(
    private val getAchievementsUseCase: GetAchievementsUseCase,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AchievementsUiState>(AchievementsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val userId = sessionRepository.userId.first() ?: run {
                _uiState.update { AchievementsUiState.Error("Сессия не найдена") }
                return@launch
            }
            getAchievementsUseCase(userId)
                .onSuccess { list -> _uiState.update { AchievementsUiState.Content(list) } }
                .onFailure { e -> _uiState.update { AchievementsUiState.Error(e.message ?: "Ошибка загрузки") } }
        }
    }
}
