package com.example.simplyachivs.presentation.login

sealed interface LoginIntent {
    data class NameChanged(val name: String) : LoginIntent
    object Continue : LoginIntent
}
