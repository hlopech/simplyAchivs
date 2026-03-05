package com.example.simplyachivs.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.analytics.AnalyticsData
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.usecase.analytics.GetAnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AnalyticsUiState {
    object Loading : AnalyticsUiState
    data class Error(val message: String) : AnalyticsUiState
    data class Content(val data: AnalyticsData) : AnalyticsUiState
}

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAnalyticsUseCase: GetAnalyticsUseCase,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnalyticsUiState>(AnalyticsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val userId = sessionRepository.userId.first() ?: run {
                _uiState.update { AnalyticsUiState.Error("Сессия не найдена") }
                return@launch
            }
            getAnalyticsUseCase(userId)
                .onSuccess { data -> _uiState.update { AnalyticsUiState.Content(data) } }
                .onFailure { e -> _uiState.update { AnalyticsUiState.Error(e.message ?: "Ошибка") } }
        }
    }
}
