package com.example.simplyachivs.presentation.asceticism

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.asceticism.Asceticism
import com.example.simplyachivs.domain.model.asceticism.AsceticismCheckIn
import com.example.simplyachivs.domain.model.asceticism.AsceticismPreset
import com.example.simplyachivs.domain.model.asceticism.AsceticismStatus
import com.example.simplyachivs.domain.model.asceticism.AsceticismWithProgress
import com.example.simplyachivs.domain.model.asceticism.ASCETICISM_PRESETS
import com.example.simplyachivs.domain.repository.AsceticismRepository
import com.example.simplyachivs.domain.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

sealed interface AsceticismUiState {
    object Loading : AsceticismUiState
    data class Error(val message: String) : AsceticismUiState
    data class Content(
        val activeItems: List<AsceticismWithProgress>,
        val historyItems: List<AsceticismWithProgress>,
        val presets: List<AsceticismPreset>,
        val tab: Int,
        val showCustomDialog: Boolean,
    ) : AsceticismUiState
}

sealed interface AsceticismIntent {
    data class SelectTab(val index: Int) : AsceticismIntent
    data class StartPreset(val preset: AsceticismPreset, val durationDays: Int) : AsceticismIntent
    data class CheckIn(val asceticismId: UUID) : AsceticismIntent
    data class Abandon(val asceticismId: UUID) : AsceticismIntent
    object OpenCustomDialog : AsceticismIntent
    object CloseCustomDialog : AsceticismIntent
    data class CreateCustom(
        val title: String,
        val description: String,
        val emoji: String,
        val durationDays: Int,
    ) : AsceticismIntent
}

sealed interface AsceticismEffect {
    object GoBack : AsceticismEffect
    data class ShowError(val message: String) : AsceticismEffect
}

@HiltViewModel
class AsceticismViewModel @Inject constructor(
    private val repository: AsceticismRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<AsceticismUiState>(AsceticismUiState.Loading)
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AsceticismEffect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        val userId = sessionRepository.userId.first() ?: run {
            _state.value = AsceticismUiState.Error("Сессия не найдена")
            return@launch
        }

        val activeRaw = repository.getActive(userId)
        // Auto-complete expired items
        activeRaw.forEach { asc ->
            if (asc.isExpired) repository.update(asc.copy(status = AsceticismStatus.COMPLETED))
        }

        val activeItems = buildWithProgress(repository.getActive(userId))
        val historyItems = buildWithProgress(repository.getHistory(userId))

        val current = _state.value
        val currentTab = if (current is AsceticismUiState.Content) current.tab else 0
        val showDialog = if (current is AsceticismUiState.Content) current.showCustomDialog else false

        _state.value = AsceticismUiState.Content(
            activeItems = activeItems,
            historyItems = historyItems,
            presets = ASCETICISM_PRESETS,
            tab = currentTab,
            showCustomDialog = showDialog,
        )
    }

    private suspend fun buildWithProgress(list: List<Asceticism>): List<AsceticismWithProgress> =
        list.map { asc -> AsceticismWithProgress(asc, repository.getCheckIns(asc.id)) }

    fun processIntent(intent: AsceticismIntent) {
        when (intent) {
            is AsceticismIntent.SelectTab -> {
                val c = _state.value as? AsceticismUiState.Content ?: return
                _state.value = c.copy(tab = intent.index)
            }
            is AsceticismIntent.StartPreset -> viewModelScope.launch {
                val userId = sessionRepository.userId.first() ?: return@launch
                repository.create(
                    Asceticism(
                        id = UUID.randomUUID(),
                        userId = userId,
                        title = intent.preset.title,
                        description = intent.preset.description,
                        emoji = intent.preset.emoji,
                        durationDays = intent.durationDays,
                        startEpochDay = LocalDate.now().toEpochDay(),
                        status = AsceticismStatus.ACTIVE,
                        createdAt = Instant.now(),
                    )
                )
                val c = _state.value as? AsceticismUiState.Content ?: return@launch
                _state.value = c.copy(tab = 0)
                load()
            }
            is AsceticismIntent.CheckIn -> viewModelScope.launch {
                repository.checkIn(
                    AsceticismCheckIn(
                        id = UUID.randomUUID(),
                        asceticismId = intent.asceticismId,
                        epochDay = LocalDate.now().toEpochDay(),
                        completedAt = Instant.now(),
                    )
                )
                load()
            }
            is AsceticismIntent.Abandon -> viewModelScope.launch {
                val c = _state.value as? AsceticismUiState.Content ?: return@launch
                val wp = c.activeItems.find { it.asceticism.id == intent.asceticismId } ?: return@launch
                repository.update(wp.asceticism.copy(status = AsceticismStatus.ABANDONED))
                load()
            }
            AsceticismIntent.OpenCustomDialog -> {
                val c = _state.value as? AsceticismUiState.Content ?: return
                _state.value = c.copy(showCustomDialog = true)
            }
            AsceticismIntent.CloseCustomDialog -> {
                val c = _state.value as? AsceticismUiState.Content ?: return
                _state.value = c.copy(showCustomDialog = false)
            }
            is AsceticismIntent.CreateCustom -> viewModelScope.launch {
                val userId = sessionRepository.userId.first() ?: return@launch
                repository.create(
                    Asceticism(
                        id = UUID.randomUUID(),
                        userId = userId,
                        title = intent.title,
                        description = intent.description,
                        emoji = intent.emoji,
                        durationDays = intent.durationDays,
                        startEpochDay = LocalDate.now().toEpochDay(),
                        status = AsceticismStatus.ACTIVE,
                        createdAt = Instant.now(),
                    )
                )
                val c = _state.value as? AsceticismUiState.Content ?: return@launch
                _state.value = c.copy(tab = 0, showCustomDialog = false)
                load()
            }
        }
    }
}
