package com.example.simplyachivs.presentation.login

sealed interface LoginEffect {
    object NavigateToHome : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
