package com.example.simplyachivs.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow<ProfileUiState>(ProfileUiState())
    val state = _state.asStateFlow()


    private val _effect = MutableSharedFlow<ProfileEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch {
            _effect.emit(effect)

        }
    }

    fun processIntent(intent: ProfileIntent) {

        when (intent) {
            ProfileIntent.OpenEditDialog -> sendEffect(ProfileEffect.ShowDialog)

            ProfileIntent.CloseEditDialog -> sendEffect(ProfileEffect.HideDialog)

            is ProfileIntent.ConfirmEdition -> {
                // update user info logic
                sendEffect(ProfileEffect.HideDialog)
                TODO()

            }


            ProfileIntent.GoBack -> {
                sendEffect(ProfileEffect.GoBack)
            }

            is ProfileIntent.SelectOption -> TODO()
        }


    }

}