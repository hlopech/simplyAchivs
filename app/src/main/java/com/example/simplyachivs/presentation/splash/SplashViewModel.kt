package com.example.simplyachivs.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        checkSession()
    }

    fun processIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.CheckSession -> checkSession()
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            delay(2000L)
            val userId = sessionRepository.userId.first()
            if (userId == null) {
                _effect.emit(SplashEffect.NavigateToLogin)
                return@launch
            }
            // Проверяем, что юзер реально существует в базе.
            // Если DB была пересоздана (schema mismatch) — юзера нет, сбрасываем сессию.
            val userExists = runCatching { userRepository.getUser(userId) }.isSuccess
            if (userExists) {
                _effect.emit(SplashEffect.NavigateToHome)
            } else {
                sessionRepository.clearSession()
                _effect.emit(SplashEffect.NavigateToLogin)
            }
        }
    }
}
