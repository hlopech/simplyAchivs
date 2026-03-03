package com.example.simplyachivs.presentation.login

data class LoginUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
