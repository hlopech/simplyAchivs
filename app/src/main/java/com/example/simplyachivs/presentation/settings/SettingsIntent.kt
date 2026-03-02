package com.example.simplyachivs.presentation.settings

sealed interface SettingsIntent {
    object GoBack : SettingsIntent
    data class ToggleNotifications(val enabled: Boolean) : SettingsIntent
    data class ToggleSound(val enabled: Boolean) : SettingsIntent
}
