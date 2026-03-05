package com.example.simplyachivs.presentation.profile

import android.net.Uri

sealed interface ProfileIntent {
    object GoBack : ProfileIntent

    object OpenEditDialog : ProfileIntent

    data class ConfirmEdition(val newName: String, val newPhoto: Uri?) : ProfileIntent

    object CloseEditDialog : ProfileIntent

    data class SelectOption(val optionId: Int) : ProfileIntent

    object OpenSettings : ProfileIntent
    object OpenAchievements : ProfileIntent
    object OpenAnalytics : ProfileIntent
    object OpenAsceticism : ProfileIntent
}