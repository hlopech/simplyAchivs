package com.example.simplyachivs.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.user.User
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.repository.UserRepository
import com.example.simplyachivs.domain.usecase.progress.CreateUserProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val createUserProgressUseCase: CreateUserProgressUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.NameChanged -> _state.update { it.copy(name = intent.name, error = null) }
            LoginIntent.Continue -> createUser()
        }
    }

    private fun createUser() {
        val name = _state.value.name.trim()
        if (name.isBlank()) {
            _state.update { it.copy(error = "Введите ваше имя") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val userId = UUID.randomUUID()
                val now = Instant.now()
                userRepository.createUser(
                    User(id = userId, name = name, avatar = "", createdAt = now, lastActiveAt = now)
                )
                createUserProgressUseCase(userId)
                sessionRepository.saveUserId(userId)
                _effect.emit(LoginEffect.NavigateToHome)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Что-то пошло не так") }
            }
        }
    }
}
