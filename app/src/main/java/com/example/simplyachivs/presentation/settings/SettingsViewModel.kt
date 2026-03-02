package com.example.simplyachivs.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private fun sendEffect(effect: SettingsEffect) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    fun processIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.GoBack -> sendEffect(SettingsEffect.GoBack)

            is SettingsIntent.ToggleNotifications ->
                _state.update { it.copy(notificationsEnabled = intent.enabled) }

            is SettingsIntent.ToggleSound ->
                _state.update { it.copy(soundEnabled = intent.enabled) }
        }
    }
}
