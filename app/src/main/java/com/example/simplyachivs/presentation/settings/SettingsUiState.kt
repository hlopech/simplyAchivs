package com.example.simplyachivs.presentation.settings

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val selectedLanguage: String = "Русский"
)
