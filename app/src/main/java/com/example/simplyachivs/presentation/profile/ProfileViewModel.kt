package com.example.simplyachivs.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.usecase.progress.GetUserProgressUseCase
import com.example.simplyachivs.domain.usecase.user.LoadUserUseCase
import com.example.simplyachivs.domain.usecase.user.UpdateUserUseCase
import com.example.simplyachivs.presentation.shop.addAward.AddAwardEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loadUserUseCase: LoadUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserProgressUseCase: GetUserProgressUseCase,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileUiState>(ProfileUiState())
    val state = _state.asStateFlow()


    private val _effect = MutableSharedFlow<ProfileEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch {
            _effect.emit(effect)

        }
    }

    init {
        viewModelScope.launch {
            val userId = sessionRepository.userId.first()
            if (userId == null) {
                sendEffect(ProfileEffect.ShowError("Сессия не найдена. Перезапустите приложение"))
                return@launch
            }

            getUserProgressUseCase(userId).onSuccess { progress ->
                _state.value = _state.value.copy(progress = progress)
            }.onFailure {
                sendEffect(ProfileEffect.ShowError("Произошла ошибка. Прогресс не найден."))

            }

            loadUserUseCase(userId).onSuccess { user ->
                _state.value = _state.value.copy(user = user)
            }.onFailure {
                sendEffect(ProfileEffect.ShowError("Произошла ошибка. Пользователь не найден."))

            }
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

            ProfileIntent.OpenSettings -> sendEffect(ProfileEffect.NavigateToSettings)
            ProfileIntent.OpenAchievements -> sendEffect(ProfileEffect.NavigateToAchievements)
            ProfileIntent.OpenAnalytics -> sendEffect(ProfileEffect.NavigateToAnalytics)
            ProfileIntent.OpenAsceticism -> sendEffect(ProfileEffect.NavigateToAsceticism)
        }


    }

}